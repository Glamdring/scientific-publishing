package com.scipub.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scipub.service.PublicationService;

@Controller
public class HomeController {

    @Inject
    private UserContext userContext;
    
    @Inject
    private PublicationService publicationService;
    
    @RequestMapping("/")
    public String homePage(Model model) {
        userContext.getUser();
        model.addAttribute("publications", publicationService.getLatestPublicationsForUser(userContext.getUser()));
        return "index";
    }
}
