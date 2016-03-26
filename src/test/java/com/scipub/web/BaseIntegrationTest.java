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
