package com.scipub.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
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
import com.scipub.util.FormatConverter.Format;
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
    
    @Transactional
    public void submitPaper(PublicationSubmissionDto dto, String userId) {
        User user = dao.getById(User.class, userId);
        if (dto.getUri() != null && !userService.isAuthor(user, dto.getUri())) {
            throw new IllegalStateException("User is not an author of the paper he's submitting");
        }
        
        Publication publication = dtoToEntity(dto, user);
        
        assignIdentifiers(dto, userId);
        
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
        
        // create revision
        PublicationRevision revision = saveRevision(dto, publication, user, revisions);
        
        if (dto.getStatus() == PublicationStatus.PUBLISHED) {
            publication.setCurrentRevision(revision);
        }
        
        if (user.getArxivUsername() != null && dto.isPushToArxiv()) {
            pushToArxiv(dto, user);
        }
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
            publications.put(branchDao.getById(Branch.class, branchId).getName(), 
                             dao.getLatestPapers(branchId, PAPERS_PER_BRANCH));
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
        Publication paper = null;
        if (dto.getUri() != null) {
            paper = dao.getById(Publication.class, dto.getUri());
        }
        if (paper == null) {
            paper = new Publication();
            paper.setCreated(LocalDateTime.now());
        }
        
        for (String authorId : dto.getAuthorIds()) {
            paper.getAuthors().add(dao.getById(User.class, authorId));
        }
        for (String nonRegisteredAuthorName : dto.getNonRegisteredAuthors()) {
            paper.getNonRegisteredAuthors().add(nonRegisteredAuthorName);
        }
        
        if (dto.getFollowUpTo() != null) {
            paper.setFollowUpTo(dao.getById(Publication.class, dto.getFollowUpTo()));
        }
        
        for (Integer branchId : dto.getBranchIds()) {
            paper.getBranches().add(dao.getById(Branch.class, branchId));
        }
        
        for (String tag : dto.getTags()) {
            //TODO fetch tags and set
        }
        
        paper.setFollowUpToLink(dto.getFollowUpToLink());
        paper.setFollowUpToDoi(dto.getFollowUpToDoi());
        paper.setStatus(dto.getStatus());
        
        return paper;
    }

    private PublicationRevision saveRevision(PublicationSubmissionDto dto, Publication paper, User user, List<PublicationRevision> currentRevisions) {
        int revisionIdx = currentRevisions.size() + 1;
        PublicationRevision lastRevision = currentRevisions.get(currentRevisions.size() - 1);
        PublicationRevision revision;
        
        // if the last revision is not the latest published, it means it's a draft we want to override
        if (!lastRevision.isLatestPublished()) {
            revision = lastRevision;
        } else {
            revision = new PublicationRevision();
        }
        
        revision.setCreated(LocalDateTime.now());
        // set as latest only if it's not a draft; otherwise it should not be visible
        revision.setLatestPublished(dto.getStatus() == PublicationStatus.PUBLISHED ? true : false);
        revision.setPublication(paper);
        revision.setOriginalFilename(dto.getOriginalFilename());
        revision.setContent(dto.getContent()); //extracted by convertContentToMarkdown
        revision.setPublicationAbstract(dto.getPaperAbstract());
        revision.setTitle(dto.getTitle());
        revision.setRevision(revisionIdx);
        revision.setSubmitter(user);
        
        return dao.persist(revision);
    }
    
    private void assignIdentifiers(PublicationSubmissionDto dto, String userId) {
        if (dto.getUri() == null) {
            dto.setUri(UriUtils.generateUri());
        }
        
        // fill-in the link based on the doi
        if (dto.getFollowUpToDoi() != null && dto.getFollowUpToLink() == null) {
            dto.setFollowUpToLink(UriUtils.getDoiUri(dto.getFollowUpToDoi()));
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
}
