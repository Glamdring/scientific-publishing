package com.scipub.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.springframework.test.util.ReflectionTestUtils;

import com.scipub.dao.jpa.PublicationDao;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;
import com.scipub.model.PublicationStatus;
import com.scipub.model.User;

public class PublicationServiceTest {

    private static String USER_ID = UUID.randomUUID().toString(); 
    @Test
    public void submitPublicationTest() {
        PublicationDao dao = mock(PublicationDao.class);
        PublicationService service = createMockService(dao);
        
        PublicationSubmissionDto dto = new PublicationSubmissionDto();
        dto.setStatus(PublicationStatus.PUBLISHED);
        dto.setTitle("Foo");

        String uri = service.submitPublication(dto, USER_ID);
        assertThat(uri, is(notNullValue()));
        
        PublicationRevision revision = new PublicationRevision();
        revision.setContent("foo");
        revision.setLatestPublished(true);
        Publication p = new Publication();
        p.setUri(UUID.randomUUID().toString());
        revision.setPublication(p);
        
        when(dao.getRevisions(any())).thenReturn(Collections.singletonList(revision));
        String uri2 = service.submitPublication(dto, USER_ID);
        
        // 1 time for the first submission with no previous revisions, and 2 times for the next one (one to save the old
        // revision as non-latest, and one for the new)
        verify(dao, times(3)).persist(isA(PublicationRevision.class));
    }

    public void submitDraftTest() {
        // test new draft revision is created
        // test overriding of the draft
        // test publishing of the draft
    }
    
    private PublicationService createMockService(PublicationDao dao) {
        
        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("Test user");
        user.setEmail("aa@aa.com");
        
        when(dao.getRevisions(any())).thenReturn(Collections.emptyList());
        when(dao.getById(User.class, USER_ID)).thenReturn(user);
        
        PublicationService service = new PublicationService();
        ReflectionTestUtils.setField(service, "dao", dao);
        return service;
    }
}
