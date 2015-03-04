package com.scipub.service;

import java.io.File;
import java.io.IOException;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.scipub.dto.PaperSubmissionDto;
import com.scipub.util.Pandoc;
import com.scipub.util.Pandoc.Format;

@Service
public class PaperService {

    @Value("${pandoc.conversion.dir}")
    private String pandocConversionDir;
    
    @Transactional
    public void submitPaper(PaperSubmissionDto dto) {
        //TODO generate DOI
        
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
