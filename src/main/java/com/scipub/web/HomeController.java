package com.scipub.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    @Inject
    private UserContext userContext;
    
    @RequestMapping("/")
    public String homePage(Model model) {
        userContext.getUser();
        return "index";
    }
}
