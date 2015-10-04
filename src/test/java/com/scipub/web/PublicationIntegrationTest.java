package com.scipub.web;

import java.util.UUID;

import javax.inject.Inject;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.google.common.collect.Lists;
import com.scipub.dto.RegistrationDto;
import com.scipub.model.User;
import com.scipub.service.ForgetUserService;
import com.scipub.service.PublicationService;
import com.scipub.service.UserService;

/**
 * End-to-end integration test
 * @author bozhanov
 *
 */
@ContextConfiguration(locations={"classpath:/spring/mvc-core-config.xml", "classpath:/spring/services.xml"})
@RunWith(SpringJUnit4ClassRunner.class)
public class PublicationIntegrationTest {

    private static final Long BRANCH_ID = 3L;
    
    @Inject
    private PublicationService publicationService;

    @Inject
    private ForgetUserService forgetUserService;
    
    @Inject
    private UserService userService;
    
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
            ex.printStackTrace();
        }
    }
    
    @After
    public void tearDown() {
        forgetUserService.foretUser(user.getId());
    }
    
    @Test
    public void paperSubmissionIntegrationTest() {
        
    }
}
