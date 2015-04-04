package com.scipub.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
import com.scipub.model.User;
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
    
    @Transactional
    public void submitPaper(PublicationSubmissionDto dto, String userId) {
        User user = dao.getById(User.class, userId);
        if (dto.getUri() != null && !userService.isAuthor(user, dto.getUri())) {
            throw new IllegalStateException("User is not an author of the paper he's submitting");
        }
        
        Publication publication = dtoToEntity(dto, user);
        
        assignIdentifiers(dto, userId);
        
        setParentBranches(publication);
        
        // for non-markdown submissions
        if (dto.getOriginalFilename() != null) {
            convertContentToMarkdown(dto);
        }
        
        // save paper
        dao.persist(publication);

        // set old revisions as old
        List<PublicationRevision> revisions = dao.getRevisions(publication);
        for (PublicationRevision revision : revisions) {
            revision.setLatest(false);
            dao.persist(revision);
        }
        
        // create revision
        saveRevision(dto, publication, user, revisions.size() + 1);
        
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

    private void saveRevision(PublicationSubmissionDto dto, Publication paper, User user, int revisionIdx) {
        PublicationRevision revision = new PublicationRevision();
        revision.setCreated(LocalDateTime.now());
        revision.setLatest(true);
        revision.setPublication(paper);
        revision.setOriginalFilename(dto.getOriginalFilename());
        revision.setContent(dto.getContent()); //extracted by convertContentToMarkdown
        revision.setPublicationAbstract(dto.getPaperAbstract());
        revision.setTitle(dto.getTitle());
        revision.setRevision(revisionIdx);
        revision.setSubmitter(user);
        
        dao.persist(revision);
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

    private void convertContentToMarkdown(PublicationSubmissionDto dto) {
        String extension = Files.getFileExtension(dto.getOriginalFilename());
        Format format = Format.forExtension(extension);
        if (format == null) {
            throw new IllegalStateException("Unsupported extension " + extension);
        }
        try {
            File in = new File(pandocConversionDir, dto.getUri() + "." + extension);
            Files.write(dto.getOriginalFileContent(), in);
            File out = new File(pandocConversionDir, dto.getUri() + ".md");
            //TODO? Pandoc.convert(format, Format.MARKDOWN, in, out, Collections.<String>emptyList());
            String content = Files.toString(out, Charsets.UTF_8);
            dto.setContent(content);
        } catch (IOException e) {
            throw new IllegalStateException(e);
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
}
