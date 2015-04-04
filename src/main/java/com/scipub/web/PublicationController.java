package com.scipub.web;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scipub.dto.PublicationSubmissionDto;
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
    
    
    @ModelAttribute("scienceBranchesJson")
    public String getScienceBranchesJson() {
        return branchesJson;
    }
}
