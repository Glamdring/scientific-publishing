package com.scipub.service;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.test.util.ReflectionTestUtils;

import com.google.common.collect.Sets;
import com.scipub.dao.jpa.BranchDao;
import com.scipub.dao.jpa.PublicationDao;
import com.scipub.dto.PublicationSubmissionDto;
import com.scipub.model.Branch;
import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;
import com.scipub.model.PublicationStatus;
import com.scipub.model.User;

public class PublicationServiceTest {

    private static UUID USER_ID = UUID.randomUUID();
    
    @Test
    public void submitPublicationTest() {
        PublicationDao dao = mock(PublicationDao.class);
        BranchDao branchDao = mock(BranchDao.class);
        when(branchDao.getById(eq(Branch.class), any())).thenReturn(new Branch());
        
        PublicationService service = createMockService(dao, branchDao);
        
        PublicationSubmissionDto dto = new PublicationSubmissionDto();
        dto.setStatus(PublicationStatus.PUBLISHED);
        dto.setTitle("Foo");
        dto.setAuthorIds(Sets.newHashSet("authorId"));
        dto.setNonRegisteredAuthors(Sets.newHashSet("name"));
        dto.setFollowUpTo("followUpToUri");
        dto.setBranchIds(Sets.newHashSet(1));
        
        String uri = service.submitPublication(dto, USER_ID);
        assertThat(uri, is(notNullValue()));
        
        PublicationRevision revision = new PublicationRevision();
        revision.setContent("foo");
        revision.setLatestPublished(true);
        Publication p = new Publication();
        p.setUri(UUID.randomUUID().toString());
        revision.setPublication(p);
        
        when(dao.getRevisions(any())).thenReturn(Collections.singletonList(revision));
        
        
        service.submitPublication(dto, USER_ID);
        
        // 1 time for the first submission with no previous revisions, and 2 times for the next one (one to save the old
        // revision as non-latest, and one for the new)
        verify(dao, times(3)).persist(isA(PublicationRevision.class));
    }

    @Test
    public void submitDraftTest() {
        PublicationDao dao = mock(PublicationDao.class);
        PublicationService service = createMockService(dao);
        
        PublicationSubmissionDto dto = new PublicationSubmissionDto();
        dto.setStatus(PublicationStatus.DRAFT);
        dto.setTitle("Foo");

        String uri = service.submitPublication(dto, USER_ID);
        assertThat(uri, is(notNullValue()));

        ArgumentCaptor<PublicationRevision> argument = ArgumentCaptor.forClass(PublicationRevision.class);
        // test new draft revision is created
        verify(dao, atLeastOnce()).persist(argument.capture());
        assertThat(argument.getValue().isLatestPublished(), is(false));
    }
    
    @Test
    public void overrideDraft() {
        PublicationDao dao = mock(PublicationDao.class);
        PublicationService service = createMockService(dao);
        
        PublicationSubmissionDto dto = new PublicationSubmissionDto();
        dto.setStatus(PublicationStatus.DRAFT);
        dto.setTitle("Foo");
        
        // test overriding of the draft
        service.submitPublication(dto, USER_ID);
        ArgumentCaptor<PublicationRevision> argument = ArgumentCaptor.forClass(PublicationRevision.class);
        verify(dao, atLeastOnce()).persist(argument.capture());
        assertThat(argument.getValue().isLatestPublished(), is(false));
    }
    
    @Test
    public void publishDraft() {
        PublicationDao dao = mock(PublicationDao.class);
        PublicationService service = createMockService(dao);
        
        PublicationSubmissionDto dto = new PublicationSubmissionDto();
        dto.setStatus(PublicationStatus.DRAFT);
        dto.setTitle("Foo");

        String uri = service.submitPublication(dto, USER_ID);
        assertThat(uri, is(notNullValue()));
        
        // test publishing a draft
        PublicationRevision revision = new PublicationRevision();
        revision.setContent("foo");
        revision.setLatestPublished(false);
        Publication p = new Publication();
        p.setUri(UUID.randomUUID().toString());
        revision.setPublication(p);
        when(dao.getRevisions(any())).thenReturn(Collections.singletonList(revision));
        
        dto.setUri(p.getUri());
        dto.setStatus(PublicationStatus.PUBLISHED);
        
        service.submitPublication(dto, USER_ID);
        
        ArgumentCaptor<PublicationRevision> argument = ArgumentCaptor.forClass(PublicationRevision.class);
        verify(dao, atLeastOnce()).persist(argument.capture());
        
        // double-cast due to mockito's inability to have multiple argument captors for multiple classes
        assertThat(((Publication) (Object) argument.getAllValues().get(2)).getStatus(), is(PublicationStatus.PUBLISHED));
        assertThat(argument.getValue().isLatestPublished(), is(true));
        assertThat(argument.getValue().getRevision(), is(2));
    }

    
    @Test
    public void getPublicationTest() {
        // very simple test to see if the dao is actually called
        PublicationDao dao = mock(PublicationDao.class);
        PublicationService service = createMockService(dao);
        String uri = "uri";
        service.getPublication(uri);
        verify(dao).getById(Publication.class, uri);
    }
    
    private PublicationService createMockService(PublicationDao dao) {
        return createMockService(dao, null);
    }
    
    private PublicationService createMockService(PublicationDao dao, BranchDao branchDao) {
        User user = new User();
        user.setId(USER_ID);
        user.setFirstName("Test user");
        user.setEmail("aa@aa.com");
        
        when(dao.getRevisions(any())).thenReturn(Collections.emptyList());
        when(dao.getById(User.class, USER_ID)).thenReturn(user);
        
        UserService mockUserService = mock(UserService.class);
        when(mockUserService.isAuthor(any(), any())).thenReturn(true);
        
        PublicationService service = new PublicationService();
        ReflectionTestUtils.setField(service, "dao", dao);
        ReflectionTestUtils.setField(service, "userService", mockUserService);
        if (branchDao != null) {
            ReflectionTestUtils.setField(service, "branchDao", branchDao);
        }
        return service;
    }
}
