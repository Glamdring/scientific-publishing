package com.scipub.web;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import com.google.common.collect.Lists;
import com.scipub.dto.RegistrationDto;
import com.scipub.model.User;
import com.scipub.service.ForgetUserService;
import com.scipub.service.UserService;

@ContextConfiguration(locations={"classpath:/spring/mvc-core-config.xml", "classpath:/spring/services.xml"})
public abstract class BaseIntegrationTest extends AbstractJUnit4SpringContextTests {

    static final Long BRANCH_ID = 3L;
    
    @Inject
    private ForgetUserService forgetUserService;
    
    @Inject UserService userService;
    
    protected User registerUser() {
        try {
            RegistrationDto registration = new RegistrationDto();
            registration.setDegree("PhD");
            registration.setEmail(UUID.randomUUID() + "@example.com");
            registration.setFirstName("John");
            registration.setLastName("Doe" + UUID.randomUUID());
            registration.setBranchIds(Lists.newArrayList(BRANCH_ID));
            return userService.completeUserRegistration(registration);
        } catch (Exception ex) {
            throw new RuntimeException("Failure during setup", ex);
        }
    }

    protected void forgetUser(User user) {
        try {
            forgetUserService.foretUser(user.getId());
        } catch (Exception ex) {
            throw new RuntimeException("Failure during tear-down. Manual cleanup needed", ex);
        }
    }
    
    protected UserContext createUserContext(User user) {
        UserContext ctx = new UserContext();
        ctx.setUser(user);
        return ctx;
    }
}
