package com.scipub.service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import org.hibernate.StaleStateException;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.scipub.dao.jpa.UserDao;
import com.scipub.dto.RegistrationDto;
import com.scipub.model.Branch;
import com.scipub.model.Publication;
import com.scipub.model.SocialAuthentication;
import com.scipub.model.User;
import com.scipub.service.auth.JpaConnectionRepository;
import com.scipub.util.SecurityUtils;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    
    @Inject
    private UserDao dao;

    @Transactional(readOnly = true)
    public boolean isAuthor(User user, String paperUri) {
        Publication paper = dao.getById(Publication.class, paperUri);
        return paper.getAuthors().contains(user);
    }

    @Transactional(readOnly = true)
    public User getUser(String userId) {
        return dao.getById(User.class, userId);
    }

    @Transactional
    public void fillUserWithNewTokens(User user, String series) {
        user.setLoginToken(UUID.randomUUID().toString());
        user.setLoginSeries(series != null ? series : UUID.randomUUID().toString());
        user.setLastLoginTime(LocalDateTime.now());
        dao.persist(user);
    }

    @Value("${hmac.key}")
    private String hmacKey;

    @Transactional
    public void connect(Long userId, SocialAuthentication auth) {
        List<SocialAuthentication> existingAuths =
                dao.getSocialAuthentications(auth.getProviderId(), auth.getProviderUserId());
        if (existingAuths.isEmpty()) {
            User user = dao.getById(User.class, userId);
            auth.setUser(user);
            dao.persist(auth);
        } else {
            SocialAuthentication existingAuth = existingAuths.get(0);
            existingAuth.setExpirationTime(auth.getExpirationTime());
            existingAuth.setRefreshToken(auth.getRefreshToken());
            existingAuth.setImageUrl(auth.getImageUrl());
            dao.persist(existingAuth);
        }
    }

    @Transactional
    public void deleteSocialAuthentication(Long userId, String providerId) {
        dao.deleteSocialAuthentication(userId, providerId);
    }

    @Transactional(readOnly = true)
    public User getUser(long id) {
        return dao.getById(User.class, id);
    }

    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return dao.getByPropertyValue(User.class, "email", email);
    }

    @Transactional
    public User completeUserRegistration(RegistrationDto dto) {
        User user = new User();
        user.setEmail(dto.getEmail());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setDegree(dto.getDegree());
        user.setLoginAutomatically(dto.isLoginAutomatically());
        for (long branchId : dto.getBranchIds()) {
            user.getBranches().add(dao.getById(Branch.class, branchId));
        }
        user.setRegistrationTime(LocalDateTime.now());
        
        user = dao.persist(user);
        if (dto.getConnection() != null) {
            SocialAuthentication auth = JpaConnectionRepository.connectionToAuth(dto.getConnection());
            auth.setUser(user);
            dao.persist(auth);
        }
        return user;
    }

    @Transactional
    public void unsubscribe(long id, String hash) {
        User user = dao.getById(User.class, id);
        if (hash.equals(SecurityUtils.hmac(user.getEmail(), hmacKey))) {
            //user.setReceiveDailyDigest(false); TODO implement daily digests?
            dao.persist(user);
        }
    }

    /**
     * http://jaspan.com/improved_persistent_login_cookie_best_practice
     */
    @Transactional(rollbackFor = StaleStateException.class)
    public User rememberMeLogin(String token, String series) {
        User existingLogin = dao.getLoginFromAuthToken(token, series);
        if (existingLogin == null) {
            User loginBySeries = dao.getByPropertyValue(User.class, "loginSeries", series);
            // if a login series exists, assume the previous token was stolen, so deleting all persistent logins.
            // An exception is a request made within a few seconds from the last login time
            // which may mean request from the same browser that is not yet aware of the renewed cookie
            // Note: fallback to joda-time, as java.time is insufficient 
            if (loginBySeries != null && new Period(new DateTime(loginBySeries.getLastLoginTime().toInstant(ZoneOffset.UTC).toEpochMilli()), 
                    DateTime.now()).getSeconds() < 5) {
                logger.info("Assuming login cookies theft; deleting all sessions for user " + loginBySeries);
                loginBySeries.setLoginSeries(null);
                loginBySeries.setLoginToken(null);
                dao.persist(loginBySeries);
            } else if (logger.isDebugEnabled()) {
                logger.debug("No existing login found for token=" + token + ", series=" + series);
            }
            return null;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Existing login found for token=" + token + " and series=" + series);
        }
        fillUserWithNewTokens(existingLogin, series);
        return existingLogin;
    }

    @Transactional(readOnly = true)
    public SocialAuthentication getTwitterAuthentication(User user) {
        if (user == null) {
            return null;
        }
        SocialAuthentication auth = dao.getTwitterAuthentication(user);
        return auth;
    }
}
