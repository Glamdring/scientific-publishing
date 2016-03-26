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
package com.scipub.service.auth;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;

import com.google.common.collect.Lists;
import com.scipub.dao.jpa.UserDao;
import com.scipub.model.SocialAuthentication;
import com.scipub.service.UserService;

public class SocialUsersConnectionRepository implements UsersConnectionRepository {
    @Inject
    private UserDao userDao;

    @Inject
    private UserService userService;

    @Inject
    private ConnectionFactoryLocator locator;
    @Override
    public List<String> findUserIdsWithConnection(Connection<?> connection) {
        List<SocialAuthentication> auths = userDao.getSocialAuthentications(connection.getKey()
                .getProviderId(), connection.getKey().getProviderUserId());
        List<UUID> userIds = Lists.newArrayList();
        for (SocialAuthentication auth : auths) {
            userIds.add(auth.getUser().getId());
        }
        return userIds.stream().map(id -> id.toString()).collect(Collectors.toList());
    }

    @Override
    public Set<String> findUserIdsConnectedTo(String providerId, Set<String> providerUserIds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ConnectionRepository createConnectionRepository(String userId) {
        return new JpaConnectionRepository(UUID.fromString(userId), userService, userDao, locator);
    }
}
