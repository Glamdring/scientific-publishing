package com.scipub.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.model.Language;
import com.scipub.model.Publication;
import com.scipub.service.PublicationService;
import com.scipub.tools.BranchJsonGenerator;

@Controller
@RequestMapping("/publication")
public class PublicationController {

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
}
