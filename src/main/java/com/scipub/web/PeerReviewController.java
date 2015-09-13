package com.scipub.web;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.scipub.dto.PeerReviewDto;
import com.scipub.service.PeerReviewService;

@Controller
@RequestMapping("/peerReview")
/**
 * Web interface for handling peer reviews
 * @author bozhanov
 */
public class PeerReviewController {

    @Inject
    private PeerReviewService peerReviewService;
    
    @Inject
    private UserContext userContext;
    
    @RequestMapping("/submit")
    @ResponseBody
    private void submitReview(PeerReviewDto dto, HttpSession session) {
        peerReviewService.submitPeerReview(dto, userContext.getUser().getId());
    }
}
