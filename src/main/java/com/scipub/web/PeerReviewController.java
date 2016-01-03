package com.scipub.web;

import javax.inject.Inject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public void submitReview(PeerReviewDto dto) {
        peerReviewService.submitPeerReview(dto, userContext.getUser().getId());
    }
    
    @RequestMapping("/submitPreliminary")
    @ResponseBody
    public void submitPreliminaryReview(@RequestParam String publicationUri, @RequestParam boolean acceptable) {
        peerReviewService.submitPreliminaryReview(userContext.getUser().getId(), publicationUri, acceptable);
    }
}
