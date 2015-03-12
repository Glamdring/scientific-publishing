package com.scipub.service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.scipub.dao.jpa.BranchDao;
import com.scipub.dao.jpa.PaperDao;
import com.scipub.dto.PaperSubmissionDto;
import com.scipub.model.Branch;
import com.scipub.model.Paper;
import com.scipub.model.PaperRevision;
import com.scipub.model.User;
import com.scipub.util.Pandoc;
import com.scipub.util.Pandoc.Format;
import com.scipub.util.UriUtils;

@Service
public class PaperService {

    private static final int PAPERS_PER_BRANCH = 3;
    
    @Value("${pandoc.conversion.dir}")
    private String pandocConversionDir;
    
    @Value("${branches.on.homepage")
    private int branchesOnHomepage;
    
    @Inject
    private UserService userService;
    
    @Inject
    private PaperDao dao;
    
    @Inject
    private BranchDao branchDao;
    
    @Transactional
    public void submitPaper(PaperSubmissionDto dto, String userId) {
        User user = dao.getById(User.class, userId);
        if (dto.getUri() != null && !userService.isAuthor(user, dto.getUri())) {
            throw new IllegalStateException("User is not an author of the paper he's submitting");
        }
        
        Paper paper = dtoToEntity(dto, user);
        
        assignIdentifiers(dto, userId);
        
        // for non-markdown submissions
        if (dto.getOriginalFilename() != null) {
            convertContentToMarkdown(dto);
        }
        
        // save paper
        dao.persist(paper);

        // set old revisions as old
        List<PaperRevision> revisions = dao.getRevisions(paper);
        for (PaperRevision revision : revisions) {
            revision.setLatest(false);
            dao.persist(revision);
        }
        
        // create revision
        saveRevision(dto, paper, user, revisions.size() + 1);
        
        if (user.getArxivUsername() != null) {
            pushToArxiv(dto, user);
        }
    }

    @Transactional(readOnly = true)
    public List<Paper> getLatestPapers(User user) {
        List<Long> branchIds = new ArrayList<Long>();
        if (user != null) {
            branchIds.addAll(user.getBranches().stream().map(b -> b.getId()).collect(Collectors.toList()));
            
            for (Branch branch : user.getBranches()) {
                addParentBranches(branch, branchIds);
            }
        }

        branchIds.addAll(branchDao.getTopLevelBranches().stream().map(b -> b.getId()).collect(Collectors.toList()));
        branchIds = branchIds.subList(0, Math.min(branchesOnHomepage, branchIds.size()));
        
        return dao.getLatestPapers(branchIds);
    }

    private void addParentBranches(Branch branch, List<Long> branchIds) {
        if (branch.getParentBranch() != null) {
            branchIds.add(branch.getParentBranch().getId());
            addParentBranches(branch.getParentBranch(), branchIds);
        }
    }
    
    private Paper dtoToEntity(PaperSubmissionDto dto, User user) {
        Paper paper = null;
        if (dto.getUri() != null) {
            paper = dao.getById(Paper.class, dto.getUri());
        }
        if (paper == null) {
            paper = new Paper();
            paper.setCreated(DateTime.now());
        }
        
        for (String authorId : dto.getAuthorIds()) {
            paper.getAuthors().add(dao.getById(User.class, authorId));
        }
        for (String nonRegisteredAuthorName : dto.getNonRegisteredAuthors()) {
            paper.getNonRegisteredAuthors().add(nonRegisteredAuthorName);
        }
        
        if (dto.getFollowUpTo() != null) {
            paper.setFollowUpTo(dao.getById(Paper.class, dto.getFollowUpTo()));
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

    private void saveRevision(PaperSubmissionDto dto, Paper paper, User user, int revisionIdx) {
        PaperRevision revision = new PaperRevision();
        revision.setCreated(DateTime.now());
        revision.setLatest(true);
        revision.setPaper(paper);
        revision.setOriginalFilename(dto.getOriginalFilename());
        revision.setContent(dto.getContent()); //extracted by convertContentToMarkdown
        revision.setPaperAbstract(dto.getPaperAbstract());
        revision.setTitle(dto.getTitle());
        revision.setRevision(revisionIdx);
        revision.setSubmitter(user);
        
        dao.persist(revision);
    }
    
    private void assignIdentifiers(PaperSubmissionDto dto, String userId) {
        if (dto.getUri() == null) {
            dto.setUri(UriUtils.generateUri());
        }
        
        // fill-in the link based on the doi
        if (dto.getFollowUpToDoi() != null && dto.getFollowUpToLink() == null) {
            dto.setFollowUpToLink(UriUtils.getDoiUri(dto.getFollowUpToDoi()));
        }
    }

    private void convertContentToMarkdown(PaperSubmissionDto dto) {
        String extension = Files.getFileExtension(dto.getOriginalFilename());
        Format format = Format.forExtension(extension);
        if (format == null) {
            throw new IllegalStateException("Unsupported extension " + extension);
        }
        try {
            File in = new File(pandocConversionDir, dto.getUri() + "." + extension);
            Files.write(dto.getOriginalFileContent(), in);
            File out = new File(pandocConversionDir, dto.getUri() + ".md");
            Pandoc.convert(format, Format.MARKDOWN, in, out, Collections.<String>emptyList());
            String content = Files.toString(out, Charsets.UTF_8);
            dto.setContent(content);
        } catch (IOException | InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }
    
    private void pushToArxiv(PaperSubmissionDto dto, User user) {
        // TODO Auto-generated method stub
    }
}
