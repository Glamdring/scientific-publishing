package com.scipub.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.aMapWithSize;
import static org.hamcrest.Matchers.hasEntry;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

import com.scipub.dto.PeerReviewDto;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.model.User;

/**
 * End-to-end integration test for peer review submissions
 * @author bozhanov
 */
public class PeerReviewIntegrationTest extends BaseIntegrationTest {

    static final Logger LOGGER = LoggerFactory.getLogger(PeerReviewIntegrationTest.class);
    
    // Controllers = entry points
    @Inject
    private PublicationController publicationController;
    
    @Inject
    private PeerReviewController peerReviewController;
    
    private User publicationOwner;
    private User peerReviewer;
    
    @Before
    public void setUp() {
        publicationOwner = registerUser();
        peerReviewer = registerUser();
    }

    @After
    public void tearDown() {
        forgetUser(publicationOwner);
        forgetUser(peerReviewer);
    }
    
    @SuppressWarnings("unchecked")
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void reviewSubmissionIntegrationTest() {
        String publicationUri = preparePublication();
        
        ReflectionTestUtils.setField(peerReviewController, "userContext", createUserContext(peerReviewer));
        // first a preliminary review
        peerReviewController.submitPreliminaryReview(publicationUri, true);
        
        // then an actual review
        PeerReviewDto submittedPeerReview = createPeerReview(publicationUri);
        peerReviewController.submitReview(submittedPeerReview);
        
        ExtendedModelMap model = new ExtendedModelMap();
        publicationController.getPublication(publicationUri, model);
        
        Map<UUID, Boolean> preliminaryReviews = (Map<UUID, Boolean>) model.get(PublicationController.PRELIMINARY_REVIEWS_KEY);
        assertThat(preliminaryReviews, aMapWithSize(1));
        assertThat(preliminaryReviews, hasEntry(peerReviewer.getId(), true));
        
        List<PeerReviewDto> peerReviews = (List<PeerReviewDto>) model.get(PublicationController.PEER_REVIEWS_KEY);
        assertThat(peerReviews, hasSize(1));
        PeerReviewDto peerReview = peerReviews.get(0);
        // the uri should be the only difference, as it is empty when submitted, so we make them equal and then compare
        submittedPeerReview.setUri(peerReview.getUri());
        
        assertThat(peerReview, is(equalTo(submittedPeerReview)));
    }

    private PeerReviewDto createPeerReview(String publicationUri) {
        PeerReviewDto dto = new PeerReviewDto();
        dto.setPublicationUri(publicationUri);
        dto.setClarityOfBackground(2);
        dto.setConflictOfInterestsDeclaration(true);
        dto.setDataAnalysis(3);
        dto.setContent("test");
        return dto;
    }

    // TODO
    //: forbid own peer review and preliminary reviews
    // check own peer and preliminary review on another user's publication
    // check if peer review and preliminary review are fetched
    // submitting a second one (duplicate) - should be rejected? or replaced
    
    private String preparePublication() {
        PublicationSubmissionDto dto = PublicationIntegrationTest.createPublication(publicationOwner);
        
        ReflectionTestUtils.setField(publicationController, "userContext", createUserContext(publicationOwner));
        
        return publicationController.submitPublication(dto);
    }
}
