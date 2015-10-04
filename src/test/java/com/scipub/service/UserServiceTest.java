package com.scipub.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.junit.Assert.fail;

import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Sets;
import com.scipub.dao.jpa.UserDao;
import com.scipub.model.Publication;
import com.scipub.model.User;

public class UserServiceTest {

    @Test
    public void isAuthorTest() {
        UserDao dao = mock(UserDao.class);
        User author = new User();
        author.setId(UUID.randomUUID());
        
        Publication ownedPublication = new Publication();
        ownedPublication.setAuthors(Sets.newHashSet(author));
        
        String ownedPublicationUri = "good";
        String notOwnedPublicationUri = "bad";
        
        when(dao.getById(Publication.class, ownedPublicationUri)).thenReturn(ownedPublication);
        when(dao.getById(Publication.class, notOwnedPublicationUri)).thenReturn(new Publication());
        UserService service = createUserService(dao);

        assertThat(service.isAuthor(author, ownedPublicationUri), is(true));
        assertThat(service.isAuthor(author, notOwnedPublicationUri), is(false));
    }

    @Test
    public void fillUserWithNewTokensTest() {
        UserDao dao = mock(UserDao.class);
        UserService service = createUserService(dao);
        
        User user = new User();
        user.setId(UUID.randomUUID());
        
        String series = "series";
        service.fillUserWithNewTokens(user, series);
        
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        verify(dao).persist(captor.capture());
        
        User persisted = captor.getValue();
        assertThat(persisted.getLoginSeries(), is(series));
        try {
            UUID.fromString(persisted.getLoginToken());
        } catch (Exception ex) {
            fail(ex.getMessage());
        }
        
    }
    private UserService createUserService(UserDao dao) {
        UserService service = new UserService();
        ReflectionTestUtils.setField(service, "dao", dao);
        return service;
    }
}
