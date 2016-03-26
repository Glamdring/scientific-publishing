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
package com.scipub.dao.jpa;

import java.util.List;
import java.util.UUID;

import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;

import com.scipub.model.SocialAuthentication;
import com.scipub.model.User;
import com.scipub.service.auth.AuthenticationProvider;

@Repository
public class UserDao extends Dao {

    public User getUserBySocialAuthentication(String providerId, String providerUserId) {
        List<SocialAuthentication> auths = getSocialAuthentications(providerId, providerUserId);
        if (auths.isEmpty()) {
            return null;
        } else {
            return auths.get(0).getUser();
        }
    }

    public List<SocialAuthentication> getSocialAuthentications(String providerId, String providerUserId) {
        TypedQuery<SocialAuthentication> query = getEntityManager().createQuery("SELECT sa FROM SocialAuthentication sa WHERE sa.providerId=:providerId AND sa.providerUserId=:providerUserId", SocialAuthentication.class);
        query.setParameter("providerId", providerId);
        query.setParameter("providerUserId", providerUserId);

        List<SocialAuthentication> auths = query.getResultList();
        return auths;
    }

    public void deleteSocialAuthentication(UUID userId, String providerId) {
        Query query = getEntityManager().createQuery("DELETE FROM SocialAuthentication WHERE user.id=:userId AND providerId=:providerId");
        query.setParameter("userId", userId);
        query.setParameter("providerId", providerId);
        query.executeUpdate();
    }

    public User getLoginFromAuthToken(String token, String series) {
        List<User> result = findByQuery(new QueryDetails<User>()
            .setQuery("SELECT user FROM User user WHERE user.loginToken=:token AND user.loginSeries=:series")
            .setParamNames(new String[] { "token", "series" })
            .setParamValues(new Object[] {token, series })
            .setResultClass(User.class));

        return getSingleResult(result);
    }

    public SocialAuthentication getTwitterAuthentication(User user) {
        List<SocialAuthentication> result = findByQuery(new QueryDetails<SocialAuthentication>()
        .setQuery("SELECT auth FROM SocialAuthentication auth WHERE auth.user=:user AND auth.providerId=:providerId")
        .setParamNames(new String[] { "user", "providerId" })
        .setParamValues(new Object[] { user, "twitter" })
        .setCount(1)
        .setResultClass(SocialAuthentication.class));

        return getSingleResult(result);
    }

    public void forgetUser(User user) {
        for (AuthenticationProvider provider : AuthenticationProvider.values()) {
            if (provider != AuthenticationProvider.PERSONA) {
                deleteSocialAuthentication(user.getId(), provider.getKey());
            }
        }
        delete(user);
    }
}
