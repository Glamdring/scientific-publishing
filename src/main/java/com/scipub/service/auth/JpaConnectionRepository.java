package com.scipub.service.auth;

import java.util.List;
import java.util.UUID;

import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionData;
import org.springframework.social.connect.ConnectionFactory;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionKey;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.NoSuchConnectionException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import com.scipub.dao.jpa.UserDao;
import com.scipub.model.SocialAuthentication;
import com.scipub.service.UserService;

public class JpaConnectionRepository implements ConnectionRepository {

    private UserService userService;
    private UserDao userDao;
    private ConnectionFactoryLocator locator;
    private UUID userId;

    public JpaConnectionRepository(UUID userId, UserService userService, UserDao userDao, ConnectionFactoryLocator locator) {
        this.userId = userId;
        this.userService = userService;
        this.userDao = userDao;
        this.locator = locator;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findAllConnections() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<Connection<?>> findConnections(String providerId) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <A> List<Connection<A>> findConnections(Class<A> apiType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public MultiValueMap<String, Connection<?>> findConnectionsToUsers(
            MultiValueMap<String, String> providerUserIds) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Connection<?> getConnection(ConnectionKey connectionKey) {
        return getConnection(connectionKey.getProviderId(), connectionKey.getProviderUserId());
    }

    @SuppressWarnings("unchecked")
    @Override
    public <A> Connection<A> getConnection(Class<A> apiType, String providerUserId) {
        String providerId = locator.getConnectionFactory(apiType).getProviderId();
        return (Connection<A>) getConnection(providerId, providerUserId);
    }

    private Connection<?> getConnection(String providerId, String providerUserId) {
        List<SocialAuthentication> socialAuthentications = userDao.getSocialAuthentications(providerId, providerUserId);
        if (socialAuthentications.isEmpty()) {
            throw new NoSuchConnectionException(new ConnectionKey(providerId, providerUserId));
        }
        return authToConnection(socialAuthentications.get(0));
    }

    @Override
    public <A> Connection<A> getPrimaryConnection(Class<A> apiType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public <A> Connection<A> findPrimaryConnection(Class<A> apiType) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    @Transactional
    public void addConnection(Connection<?> connection) {
        SocialAuthentication auth = connectionToAuth(connection);
        userService.connect(userId, auth);
    }

    @Override
    public void updateConnection(Connection<?> connection) {
        SocialAuthentication auth = connectionToAuth(connection);
        userService.connect(userId, auth);
    }

    public static SocialAuthentication connectionToAuth(Connection<?> connection) {
        SocialAuthentication auth = new SocialAuthentication();
        ConnectionData data = connection.createData();
        auth.setProviderId(data.getProviderId());
        auth.setToken(data.getAccessToken());
        auth.setRefreshToken(data.getRefreshToken());
        auth.setSecret(data.getSecret());
        auth.setProviderUserId(data.getProviderUserId());
        return auth;
    }

    private Connection<?> authToConnection(SocialAuthentication auth) {
        ConnectionFactory<?> connectionFactory = locator.getConnectionFactory(auth.getProviderId());
        ConnectionData data = new ConnectionData(auth.getProviderId(), auth.getProviderUserId(), null, null,
                auth.getImageUrl(), auth.getToken(), auth.getSecret(), auth.getRefreshToken(),
                auth.getExpirationTime());
        return connectionFactory.createConnection(data);
    }

    @Override
    public void removeConnections(String providerId) {
        userService.deleteSocialAuthentication(userId, providerId);
    }

    @Override
    public void removeConnection(ConnectionKey connectionKey) {
        userService.deleteSocialAuthentication(userId, connectionKey.getProviderId());
    }

}
