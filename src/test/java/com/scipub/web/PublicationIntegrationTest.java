package com.scipub.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

import java.util.UUID;
import java.util.stream.Collectors;

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

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.dto.RegistrationDto;
import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;
import com.scipub.model.PublicationStatus;
import com.scipub.model.User;
import com.scipub.service.ForgetUserService;
import com.scipub.service.UserService;
import com.scipub.web.util.Constants;

/**
 * End-to-end integration test
 * @author bozhanov
 */
public class PublicationIntegrationTest extends BaseIntegrationTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationIntegrationTest.class);
    
    private static final Long BRANCH_ID = 3L;
    
    @Inject
    private ForgetUserService forgetUserService;
    
    @Inject
    private UserService userService;
    
    // Controllers = entry points
    @Inject
    private PublicationController publicationController;
    
    private User user;

    @Before
    public void setUp() {
        try {
            RegistrationDto registration = new RegistrationDto();
            registration.setDegree("PhD");
            registration.setEmail(UUID.randomUUID() + "@example.com");
            registration.setFirstName("John");
            registration.setLastName("Doe" + UUID.randomUUID());
            registration.setBranchIds(Lists.newArrayList(BRANCH_ID));
            user = userService.completeUserRegistration(registration);
        } catch (Exception ex) {
            LOGGER.error("Failure during setup", ex);
        }
    }
    
    @After
    public void tearDown() {
        try {
            forgetUserService.foretUser(user.getId());
        } catch (Exception ex) {
            LOGGER.error("Failure during tear-down. Manual cleanup needed", ex);
        }
    }
    
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void publicationSubmissionIntegrationTest() {
        PublicationSubmissionDto dto = createdPublication();
        
        ReflectionTestUtils.setField(publicationController, "userContext", createUserContext());
        
        String uri = publicationController.submitPublication(dto);

        Publication publication = getPublication(uri);
        
        assertPublicationFields(dto, publication, publication.getCurrentRevision());
        assertThat(publication.getStatus(), is(PublicationStatus.PUBLISHED));
    }
    
    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public void draftSubmissionIntegrationTest() {
        PublicationSubmissionDto dto = createdPublication();
        
        ReflectionTestUtils.setField(publicationController, "userContext", createUserContext());
        
        String uri = publicationController.submitDraft(dto);
        
        Publication firstDraftPublication = getPublication(uri);
        assertThat(firstDraftPublication.getCurrentRevision(), is(nullValue()));
        
        PublicationRevision firstDraft = firstDraftPublication.getCurrentDraft();
        assertThat(firstDraft, is(notNullValue()));
        assertThat(firstDraft.getContent(), is(dto.getContent()));
        
        dto.setUri(uri);
        dto.setContent(dto.getContent() + "!");

        String secondTimeUri = publicationController.submitDraft(dto);
        assertThat(uri, is(secondTimeUri));
        
        Publication secondDraftPublication = getPublication(uri);
        assertThat(secondDraftPublication.getCurrentRevision(), is(nullValue()));
        // check that no new draft is created, but the latest existing one is updated
        assertThat(secondDraftPublication.getCurrentDraft().getId(), is(firstDraft.getId()));
        assertThat(secondDraftPublication.getCurrentDraft().getContent(), is(dto.getContent()));
        
        // first set another user and try to obtain draft. Should fail.
        UserContext anotherUserContext = createUserContext();
        User newUser = new User();
        newUser.setId(UUID.randomUUID());
        anotherUserContext.setUser(newUser);
        ReflectionTestUtils.setField(publicationController, "userContext", anotherUserContext);
        
        String redirectUri = publicationController.getPublication(uri, new ExtendedModelMap());
        assertThat(redirectUri, is(Constants.REDIRECT_HOME));
        
        // restore the original user
        ReflectionTestUtils.setField(publicationController, "userContext", createUserContext());
        Publication publication = getPublication(uri);
        
        assertPublicationFields(dto, publication, publication.getCurrentDraft());
        assertThat(publication.getStatus(), is(PublicationStatus.DRAFT));
    }

    private Publication getPublication(String uri) {
        ExtendedModelMap model = new ExtendedModelMap();
        publicationController.getPublication(uri, model);
        Publication publication = (Publication) model.get("publication");
        return publication;
    }

    private void assertPublicationFields(PublicationSubmissionDto dto, Publication publication, PublicationRevision revision) {
        assertThat(publication, is(notNullValue()));
        
        assertThat(publication.getAuthors(), contains(user));
        assertThat(publication.getBranches()
                              .stream().map(b -> b.getId())
                              .collect(Collectors.toList()), 
                  contains(BRANCH_ID));
        assertThat(publication.getPrimaryBranch().getId(), is(BRANCH_ID));
        
        assertThat(revision, is(notNullValue()));
        assertThat(revision.getTitle(), is(dto.getTitle()));
        assertThat(revision.getPublicationAbstract(), is(dto.getPublicationAbstract()));
        assertThat(revision.getContent(), is(dto.getContent()));
    }

    private PublicationSubmissionDto createdPublication() {
        PublicationSubmissionDto dto = new PublicationSubmissionDto();
        dto.setAuthorIds(Sets.newHashSet(user.getId()));
        dto.setBranchIds(Sets.newHashSet(BRANCH_ID));
        dto.setPrimaryBranch(BRANCH_ID);
        
        String content = "Test publication contest";
        String publicationAbstract = "Test abstract";
        String title = "Test title";
        
        dto.setContent(content);
        dto.setPublicationAbstract(publicationAbstract);
        dto.setTitle(title);
        return dto;
    }
    
    private UserContext createUserContext() {
        UserContext ctx = new UserContext();
        ctx.setUser(user);
        return ctx;
    }
}
