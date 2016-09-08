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

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.google.common.io.Files;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.model.Language;
import com.scipub.model.Publication;
import com.scipub.model.PublicationStatus;
import com.scipub.service.PeerReviewService;
import com.scipub.service.PublicationService;
import com.scipub.tools.BranchJsonGenerator;
import com.scipub.util.FormatConverter;
import com.scipub.util.FormatConverter.Format;
import com.scipub.web.util.Constants;
import com.scipub.web.util.RequireUserLoggedIn;

@Controller
@RequestMapping("/publication")
public class PublicationController {

    static final String PUBLICATION_KEY = "publication";
    static final String OWN_PRELIMINARY_REVIEW_KEY = "ownPreliminaryReview";
    static final String OWN_PEER_REVIEW_KEY = "ownPeerReview";
    static final String PEER_REVIEWS_KEY = "peerReviews";
    static final String PRELIMINARY_REVIEWS_KEY = "preliminaryReviews";

    private static final Logger logger = LoggerFactory.getLogger(PublicationController.class);
    
    private static final String LAST_UPLOADED_FILE_KEY = "lastUploadedFile";
    
    @Inject
    private PublicationService publicationService;
   
    @Inject
    private PeerReviewService peerReviewService;
    
    @Inject
    private UserContext userContext;

    @Inject
    private BranchController branchController;
    
    @RequestMapping("/new")
    @RequireUserLoggedIn
    public String newPublication(HttpSession session) {
        // cleanup any previously uploaded file
        session.removeAttribute(LAST_UPLOADED_FILE_KEY);
        return "newPublication";
    }
    
    @RequestMapping("/submit")
    @RequireUserLoggedIn
    public String submit(PublicationSubmissionDto dto, HttpSession session) {
        fillFileDetails(dto, session);
        String uri = submitPublication(dto);
        return "redirect:/publication?uri=" + uri;
    }
    
    String submitPublication(PublicationSubmissionDto dto) {
        dto.setStatus(PublicationStatus.PUBLISHED);
        String uri = publicationService.submitPublication(dto, userContext.getUser().getId());
        return uri;
    }
    
    @RequestMapping("/saveDraft")
    @ResponseBody
    @RequireUserLoggedIn
    public String saveDraft(PublicationSubmissionDto dto, HttpSession session) {
        fillFileDetails(dto, session);
        return submitDraft(dto);
    }
    
    String submitDraft(PublicationSubmissionDto dto) {
        dto.setStatus(PublicationStatus.DRAFT);
        return publicationService.submitPublication(dto, userContext.getUser().getId());        
    }
    
    @RequestMapping(value={"", "/"}, params="uri")
    public String getPublication(@RequestParam String uri, Model model) {
        Publication publication = publicationService.getPublication(uri);
        if (publication != null) {
            // if it's a draft and the current user is not an author, don't show
            if (publication.getStatus() == PublicationStatus.DRAFT && !publication.getAuthors().contains(userContext.getUser())) {
                return Constants.REDIRECT_HOME;
            }
            model.addAttribute(PUBLICATION_KEY, publication);
            
            if (userContext.getUser() != null) {
                // get peer reviews submitted by the current user
                peerReviewService.getPeerReview(userContext.getUser().getId(), uri)
                    .ifPresent(pr -> model.addAttribute(OWN_PEER_REVIEW_KEY, pr));
                peerReviewService.getPreliminaryReview(userContext.getUser().getId(), uri)
                    .ifPresent(acceptable -> model.addAttribute(OWN_PRELIMINARY_REVIEW_KEY, acceptable));
            }
            
            // get all peer reviews for this publication
            model.addAttribute(PRELIMINARY_REVIEWS_KEY, peerReviewService.getPreliminaryReviews(uri));
            model.addAttribute(PEER_REVIEWS_KEY, peerReviewService.getPeerReviews(uri));
            return "publication";
        } else {
            return Constants.REDIRECT_HOME;
        }
    }
    
    private void fillFileDetails(PublicationSubmissionDto dto, HttpSession session) {
        @SuppressWarnings("unchecked")
        ImmutablePair<String, byte[]> file = (ImmutablePair<String, byte[]>) session.getAttribute(LAST_UPLOADED_FILE_KEY);
        if (file != null) {
            dto.setOriginalFilename(file.getKey());
            dto.setOriginalFileContent(file.getValue());
        }
    }
    
    @RequestMapping("/uploadFile")
    @ResponseBody
    public void uploadFile(@RequestParam MultipartFile file, HttpSession session) throws IOException {
        session.setAttribute(LAST_UPLOADED_FILE_KEY, new ImmutablePair<String, byte[]>(file.getOriginalFilename(), file.getBytes()));
    }
    
    @RequestMapping("/importFile")
    @ResponseBody
    public UploadResult importFile(@RequestParam MultipartFile file, HttpSession session) throws IOException {
        //TODO store uploaded original?
        String extension = Files.getFileExtension(file.getOriginalFilename());
        logger.info("Received file " + file.getOriginalFilename() + " of type " + file.getContentType() + " and size "
                + file.getSize() + " and ext " + extension);
        byte[] md = FormatConverter.convert(Format.forExtension(extension), Format.MARKDOWN, file.getBytes());
        UploadResult result = new UploadResult();
        result.setContent(new String(md, "UTF-8"));
        
        session.setAttribute(LAST_UPLOADED_FILE_KEY, new ImmutablePair<String, byte[]>(file.getOriginalFilename(), file.getBytes()));
        
        return result;
    }
    
    @RequestMapping("/autocomplete")
    public List<PublicationSubmissionDto> autocomplete(@RequestParam String input) {
        return publicationService.findPublication(input);
    }
    
    
    @ModelAttribute("scienceBranchesJson")
    public String getScienceBranchesJson() {
        return branchController.getScienceBranchesJson();
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
