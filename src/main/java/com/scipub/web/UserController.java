package com.scipub.web;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.scipub.dto.UserDetails;
import com.scipub.service.UserService;

@Controller
@RequestMapping("/users")
public class UserController {

    @Inject
    private UserService userService;
    
    @RequestMapping("/autocomplete")
    public String suggestUsers(@RequestParam String start, Model model) {

        List<UserDetails> details = userService.findUsers(start);

        model.addAttribute("users", details);

        return "results/userSuggestions";
    }
}
