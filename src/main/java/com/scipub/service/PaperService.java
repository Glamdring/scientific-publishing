package com.scipub.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import javax.annotation.Resource;
import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.scipub.dao.jpa.Dao;
import com.scipub.dto.PaperSubmissionDto;
import com.scipub.model.User;
import com.scipub.util.Pandoc;
import com.scipub.util.Pandoc.Format;
import com.scipub.util.UriUtils;

@Service
public class PaperService {

    @Value("${pandoc.conversion.dir}")
    private String pandocConversionDir;
    
    @Inject
    private UserService userService;
    
    @Resource(name="dao")
    private Dao dao;
    
    @Transactional
    public void submitPaper(PaperSubmissionDto dto, String userId) {
        User user = dao.getById(User.class, userId);
        if (dto.getUri() == null) {
            dto.setUri(UriUtils.generateUri());
        } else {
            if (!userService.isAuthor(user, dto.getUri())) {
                throw new IllegalStateException("User is not an author of the paper he's submitting");
            }
        }
        
        // fill-in the link based on the doi
        if (dto.getFollowUpToDoi() != null && dto.getFollowUpToLink() == null) {
            dto.setFollowUpToLink(UriUtils.getDoiUri(dto.getFollowUpToDoi()));
        }
        
        // for non-markdown submissions
        if (dto.getOriginalFilename() != null) {
            convertContentToMarkdown(dto);
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
}
