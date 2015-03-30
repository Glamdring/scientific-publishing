package com.scipub.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.scipub.dto.PaperSubmissionDto;
import com.scipub.service.PublicationService;

@Controller
@RequestMapping("/paper")
public class PaperController {

    @Inject
    private PublicationService paperService;
    
    private void submit(@RequestBody PaperSubmissionDto dto) {
        
    }
}
