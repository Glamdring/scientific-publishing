package com.scipub.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

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
import com.scipub.service.PublicationService;
import com.scipub.tools.BranchJsonGenerator;
import com.scipub.util.FormatConverter;
import com.scipub.util.FormatConverter.Format;

@Controller
@RequestMapping("/publication")
public class PublicationController {

    private static final Logger logger = LoggerFactory.getLogger(PublicationController.class);
    
    @Inject
    private PublicationService publicationService;
    
    private String branchesJson; 
    
    @PostConstruct
    public void init() throws Exception {
        branchesJson = BranchJsonGenerator.getBranchJson(false).replace("'", "\\'");
    }
    @RequestMapping("/new")
    public String newPublication() {
        return "newPublication";
    }
    
    private void submit(@RequestBody PublicationSubmissionDto dto) {
        
    }
    
    
    @RequestMapping("/uploadFile")
    @ResponseBody
    public void uploadFile(@RequestParam MultipartFile file) {
        //TODO store in session
    }
    
    @RequestMapping("/importFile")
    @ResponseBody
    public UploadResult importFile(@RequestParam MultipartFile file) throws IOException {
        String extension = Files.getFileExtension(file.getOriginalFilename());
        logger.info("Received file " + file.getOriginalFilename() + " of type " + file.getContentType() + " and size "
                + file.getSize() + " and ext " + extension);
        byte[] md = FormatConverter.convert(Format.forExtension(extension), Format.MARKDOWN, file.getBytes());
        UploadResult result = new UploadResult();
        result.setContent(new String(md, "UTF-8"));
        
        //TODO store file in session?
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
