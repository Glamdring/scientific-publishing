/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.scipub.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scipub.dao.jpa.BranchDao;
import com.scipub.dao.jpa.PublicationDao;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.model.Branch;
import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;
import com.scipub.model.PublicationSource;
import com.scipub.model.PublicationStatus;
import com.scipub.model.User;
import com.scipub.service.SearchService.SearchType;
import com.scipub.util.UriUtils;

@Service
public class PublicationService {

    private static final int PAPERS_PER_BRANCH = 3;
    
    @Value("${pandoc.conversion.dir}")
    private String pandocConversionDir;
    
    @Value("${branches.on.homepage}")
    private int branchesOnHomepage;
    
    @Inject
    private UserService userService;
    
    @Inject
    private PublicationDao dao;
    
    @Inject
    private BranchDao branchDao;
    
    @Inject
    private SearchService searchService;

    /**
     * Submit the given publication for the given user id
     * @param dto the publication details
     * @param userId the id of the submitter
     * @return the URI of the saved publication
     */
    @Transactional
    public String submitPublication(PublicationSubmissionDto dto, UUID userId) {
        User user = dao.getById(User.class, userId);
        if (dto.getUri() != null && !userService.isAuthor(user, dto.getUri())) {
            throw new IllegalStateException("User is not an author of the paper he's submitting");
        }
        
        Publication publication = dtoToEntity(dto, user);
        
        assignIdentifiers(publication, dto, userId);
        
        setParentBranches(publication);
        
        // save publication
        dao.persist(publication);

        List<PublicationRevision> revisions = dao.getRevisions(publication);
        // set old revisions as old (if the new one is not just a draft)
        if (dto.getStatus() == PublicationStatus.PUBLISHED) {
            for (PublicationRevision revision : revisions) {
                revision.setLatestPublished(false);
                dao.persist(revision);
            }
        }
        
        // create/update revision
        PublicationRevision revision = saveRevision(dto, publication, user, revisions);
        
        if (dto.getStatus() == PublicationStatus.PUBLISHED) {
            publication.setCurrentRevision(revision);
            publication.setCurrentDraft(null);
            dao.persist(publication);
        } else if (dto.getStatus() == PublicationStatus.DRAFT) {
            publication.setCurrentDraft(revision);
            dao.persist(publication);
        }
        
        if (user.getArxivUsername() != null && dto.isPushToArxiv()) {
            pushToArxiv(dto, user);
        }
        
        return publication.getUri();
    }

    private void setParentBranches(Publication publication) {
        // set parent branches of all the detected branches
        for (Branch branch : publication.getBranches()) {
            while (branch.getParentBranch() != null) {
                branch = branch.getParentBranch();
                publication.getParentBranches().add(branch);
            }
        }
    }

    @Transactional(readOnly = true)
    public Map<String, List<Publication>> getLatestPublicationsForUser(User user) {
        List<Long> branchIds = new ArrayList<Long>();
        if (user != null) {
            branchIds.addAll(user.getBranches().stream().map(b -> b.getId()).collect(Collectors.toList()));
            
            for (Branch branch : user.getBranches()) {
                addParentBranches(branch, branchIds);
            }
        }

        branchIds.addAll(branchDao.getTopLevelBranches().stream().map(b -> b.getId()).collect(Collectors.toList()));
        branchIds = branchIds.subList(0, Math.min(branchesOnHomepage, branchIds.size()));
        
        Map<String, List<Publication>> publications = new LinkedHashMap<>();
        for (long branchId : branchIds) {
            Branch branch = branchDao.getById(Branch.class, branchId);
            publications.put(branch.getName(), dao.getLatestPapers(branch, PAPERS_PER_BRANCH));
        }
        return publications;
    }

    private void addParentBranches(Branch branch, List<Long> branchIds) {
        if (branch.getParentBranch() != null) {
            branchIds.add(branch.getParentBranch().getId());
            addParentBranches(branch.getParentBranch(), branchIds);
        }
    }
    
    private Publication dtoToEntity(PublicationSubmissionDto dto, User user) {
        Publication publication = null;
        if (dto.getUri() != null) {
            publication = dao.getById(Publication.class, dto.getUri());
        }
        if (publication == null) {
            publication = new Publication();
            publication.setCreated(LocalDateTime.now());
        }
        
        for (UUID authorId : dto.getAuthorIds()) {
            publication.getAuthors().add(dao.getById(User.class, authorId));
        }
        for (String nonRegisteredAuthorName : dto.getNonRegisteredAuthors()) {
            publication.getNonRegisteredAuthors().add(nonRegisteredAuthorName);
        }
        
        if (dto.getFollowUpTo() != null) {
            publication.setFollowUpTo(dao.getById(Publication.class, dto.getFollowUpTo()));
        }
        
        for (Long branchId : dto.getBranchIds()) {
            publication.getBranches().add(branchDao.getById(Branch.class, branchId));
        }
        if (dto.getPrimaryBranch() != null) {
            publication.setPrimaryBranch(branchDao.getById(Branch.class, dto.getPrimaryBranch()));
        }
        
        for (String tag : dto.getTags()) {
            //TODO fetch tags and set
        }
        
        publication.setFollowUpToLink(dto.getFollowUpToLink());
        publication.setFollowUpToDoi(dto.getFollowUpToDoi());
        publication.setStatus(dto.getStatus());
        publication.setSource(PublicationSource.SUBMITTED);
        
        return publication;
    }

    private PublicationRevision saveRevision(PublicationSubmissionDto dto, Publication publication, User user, List<PublicationRevision> currentRevisions) {
        int revisionIdx = currentRevisions.size() + 1;
        PublicationRevision lastRevision = currentRevisions.isEmpty() ? null : currentRevisions.get(currentRevisions.size() - 1);
        PublicationRevision revision;
        
        // if the last revision is not the latest published, it means it's a draft we want to override
        if (lastRevision != null && !lastRevision.isLatestPublished()) {
            revision = lastRevision;
        } else {
            revision = new PublicationRevision();
        }
        
        revision.setCreated(LocalDateTime.now());
        // set as latest only if it's not a draft; otherwise it should not be visible
        revision.setLatestPublished(dto.getStatus() == PublicationStatus.PUBLISHED ? true : false);
        revision.setPublication(publication);
        revision.setOriginalFilename(dto.getOriginalFilename());
        revision.setContent(dto.getContent()); //extracted by convertContentToMarkdown
        revision.setPublicationAbstract(dto.getPublicationAbstract());
        revision.setTitle(dto.getTitle());
        revision.setRevision(revisionIdx);
        revision.setSubmitter(user);
        
        return dao.persist(revision);
    }
    
    private void assignIdentifiers(Publication publication, PublicationSubmissionDto dto, UUID userId) {
        if (publication.getUri() == null) {
            publication.setUri(UriUtils.generateUri());
        }
        
        // fill-in the link based on the doi
        if (dto.getFollowUpToDoi() != null && dto.getFollowUpToLink() == null) {
            publication.setFollowUpToLink(UriUtils.getDoiUri(dto.getFollowUpToDoi()));
        }
    }

    @Transactional
    public void storePublication(List<Publication> publications) {
        dao.storePublications(publications);
    }
    
    private void pushToArxiv(PublicationSubmissionDto dto, User user) {
        // TODO Auto-generated method stub
    }

    @Transactional(readOnly = true)
    public LocalDateTime getLastImportDate(PublicationSource arxiv) {
        LocalDateTime lastImport = dao.getLastImportDate(arxiv);
        if (lastImport == null) {
            lastImport = LocalDateTime.now().minusWeeks(1);
        }
        return lastImport;
    }

    public List<PublicationSubmissionDto> findPublication(String input) {
        List<Publication> result = searchService.search(input, Publication.class, SearchType.FULL);
        //TODO map
        return Collections.emptyList();
    }

    @Transactional(readOnly = true)
    public Publication getPublication(String uri) {
        return dao.getById(Publication.class, uri);
    }
}
