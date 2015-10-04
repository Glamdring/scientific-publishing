package com.scipub.web;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.servlet.http.HttpSession;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ExtendedModelMap;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.dto.RegistrationDto;
import com.scipub.model.Publication;
import com.scipub.model.PublicationStatus;
import com.scipub.model.User;
import com.scipub.service.ForgetUserService;
import com.scipub.service.UserService;

/**
 * End-to-end integration test
 * @author bozhanov
 *
 */
@ContextConfiguration(locations={"classpath:/spring/mvc-core-config.xml", "classpath:/spring/services.xml"})
public class PublicationIntegrationTest extends AbstractJUnit4SpringContextTests {

    private static final Logger LOGGER = LoggerFactory.getLogger(PublicationIntegrationTest.class);
    
    private static final Long BRANCH_ID = 3L;
    
    @Inject
    private PlatformTransactionManager txMgr;
    
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
    public void paperSubmissionIntegrationTest() {
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
        
        ReflectionTestUtils.setField(publicationController, "userContext", getUserContext());
        
        String uri = publicationController.submitPublication(dto);

        ExtendedModelMap model = new ExtendedModelMap();
        publicationController.getPublication(uri, model);
        
        Publication publication = (Publication) model.get("publication");
        
        assertThat(publication, is(notNullValue()));
        
        assertThat(publication.getAuthors(), contains(user));
        assertThat(publication.getBranches()
                              .stream().map(b -> b.getId())
                              .collect(Collectors.toList()), 
                  contains(BRANCH_ID));
        assertThat(publication.getPrimaryBranch().getId(), is(BRANCH_ID));
        
        assertThat(publication.getCurrentRevision(), is(notNullValue()));
        assertThat(publication.getCurrentRevision().getTitle(), is(title));
        assertThat(publication.getCurrentRevision().getPublicationAbstract(), is(publicationAbstract));
        assertThat(publication.getCurrentRevision().getContent(), is(content));
        assertThat(publication.getStatus(), is(PublicationStatus.PUBLISHED));
    }
    
    private UserContext getUserContext() {
        UserContext ctx = new UserContext();
        ctx.setUser(user);
        return ctx;
    }
}
