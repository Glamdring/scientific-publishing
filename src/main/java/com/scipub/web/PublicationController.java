package com.scipub.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.model.Language;
import com.scipub.model.PublicationStatus;
import com.scipub.service.PublicationService;
import com.scipub.tools.BranchJsonGenerator;
import com.scipub.util.FormatConverter;
import com.scipub.util.FormatConverter.Format;

@Controller
@RequestMapping("/publication")
public class PublicationController {

    private static final Logger logger = LoggerFactory.getLogger(PublicationController.class);
    
    private static final String LAST_UPLOADED_FILE_KEY = "lastUploadedFile";
    
    @Inject
    private PublicationService publicationService;
    
    @Inject
    private UserContext userContext;
    
    private String branchesJson; 
    
    @PostConstruct
    public void init() throws Exception {
        branchesJson = BranchJsonGenerator.getBranchJson(false).replace("'", "\\'");
    }
    @RequestMapping("/new")
    public String newPublication(HttpSession session) {
        // cleanup any previously uploaded file
        session.removeAttribute(LAST_UPLOADED_FILE_KEY);
        return "newPublication";
    }
    
    @RequestMapping("/submit")
    public void submit(@RequestBody PublicationSubmissionDto dto, HttpSession session) {
        
        fillFileDetails(dto, session);
        dto.setStatus(PublicationStatus.PUBLISHED);
        publicationService.submitPaper(dto, userContext.getUser().getId());
    }
    
    @RequestMapping("/saveDraft")
    private void saveDraft(@RequestBody PublicationSubmissionDto dto, HttpSession session) {
        
        fillFileDetails(dto, session);
        dto.setStatus(PublicationStatus.DRAFT);
        publicationService.submitPaper(dto, userContext.getUser().getId());
    }
    
    private void fillFileDetails(PublicationSubmissionDto dto, HttpSession session) {
        @SuppressWarnings("unchecked")
        ImmutablePair<String, byte[]> file = (ImmutablePair<String, byte[]>) session.getAttribute(LAST_UPLOADED_FILE_KEY);
        if (file != null) {
            dto.setOriginalFilename(file.getKey());
            dto.setOriginalFileContent(file.getValue());
        }
    }
    
    @RequestMapping("/uploadFile")
    @ResponseBody
    public void uploadFile(@RequestParam MultipartFile file, HttpSession session) throws IOException {
        session.setAttribute(LAST_UPLOADED_FILE_KEY, new ImmutablePair<String, byte[]>(file.getOriginalFilename(), file.getBytes()));
    }
    
    @RequestMapping("/importFile")
    @ResponseBody
    public UploadResult importFile(@RequestParam MultipartFile file, HttpSession session) throws IOException {
        String extension = Files.getFileExtension(file.getOriginalFilename());
        logger.info("Received file " + file.getOriginalFilename() + " of type " + file.getContentType() + " and size "
                + file.getSize() + " and ext " + extension);
        byte[] md = FormatConverter.convert(Format.forExtension(extension), Format.MARKDOWN, file.getBytes());
        UploadResult result = new UploadResult();
        result.setContent(new String(md, "UTF-8"));
        
        session.setAttribute(LAST_UPLOADED_FILE_KEY, new ImmutablePair<String, byte[]>(file.getOriginalFilename(), file.getBytes()));
        
        return result;
    }
    
    @RequestMapping("/autocomplete")
    public List<PublicationSubmissionDto> autocomplete(@RequestParam String input) {
        return publicationService.findPublication(input);
    }
    
    
    @ModelAttribute("scienceBranchesJson")
    public String getScienceBranchesJson() {
        return branchesJson;
    }
    
    @ModelAttribute("languages")
    public Language[] getLanguages(){
        return Language.values();
    }
    
    public static final class UploadResult {
        private String content;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }
    }
}
