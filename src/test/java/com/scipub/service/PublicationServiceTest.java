package com.scipub.service;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Collections;

import org.springframework.test.util.ReflectionTestUtils;

import com.scipub.dao.jpa.PublicationDao;

public class PublicationServiceTest {

    
    public void submitPublicationTest() {
        PublicationDao dao = mock(PublicationDao.class);
        when(dao.getRevisions(any())).thenReturn(Collections.emptyList());
        
        PublicationService service = new PublicationService();
        ReflectionTestUtils.setField(service, "dao", dao);
        
        // test old revisions are set as not latest
        // test uri is assigned
    }
    
    public void submitDraftTest() {
        // test new draft revision is created
        // test overriding of the draft
        // test publishing of the draft
    }
}
