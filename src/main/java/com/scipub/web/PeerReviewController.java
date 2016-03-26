/**
 * Scientific publishing
 * Copyright (C) 2015-2016  Bozhidar Bozhanov
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
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
