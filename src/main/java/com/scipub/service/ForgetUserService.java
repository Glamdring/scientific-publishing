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
package com.scipub.service;

import java.util.UUID;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;
import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dao.jpa.PublicationDao;
import com.scipub.dao.jpa.UserDao;
import com.scipub.model.User;

@Service
public class ForgetUserService {

    @Inject
    private PublicationDao publicationDao;
    
    @Inject
    private UserDao userDao;
    
    @Inject
    private PeerReviewDao peerReviewDao;
    
    @Transactional
    public void foretUser(UUID userId) {
        Preconditions.checkNotNull(userId);
        User user = userDao.getById(User.class, userId);
        peerReviewDao.deleteAllReviews(user);
        publicationDao.deletePublications(user);
        //TODO comments, inline comments, resources,
        userDao.forgetUser(user);
    }
}
