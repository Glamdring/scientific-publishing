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

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import com.scipub.dao.jpa.PeerReviewDao;
import com.scipub.dto.PeerReviewDto;
import com.scipub.model.PeerReview;
import com.scipub.model.Publication;
import com.scipub.model.User;

public class PeerReviewServiceTest {

    private static UUID USER_ID = UUID.randomUUID();
    private static User user = new User();
    static {
        user.setId(USER_ID);
    }
    
    @Test
    public void submitPeerReviewTest() {
        PeerReviewService service = new PeerReviewService();
        PeerReviewDao dao = mock(PeerReviewDao.class);
        when(dao.getById(eq(User.class), any())).thenReturn(user);
        when(dao.getPeerReview(any(), any())).thenReturn(Optional.empty());
        
        Publication publication = new Publication();
        String publicationUri = "scipub:foo";
        publication.setUri(publicationUri);
        
        when(dao.persist(any())).thenAnswer(new Answer<PeerReview>(){
            @Override
            public PeerReview answer(InvocationOnMock invocation) throws Throwable {
                return invocation.getArgumentAt(0, PeerReview.class);
            }
        });
        when(dao.getById(eq(Publication.class), eq(publicationUri))).thenReturn(publication);
        ArgumentCaptor<PeerReview> argument = ArgumentCaptor.forClass(PeerReview.class);
        
        PeerReviewDto dto = new PeerReviewDto();
        String content = "foo";
        
        dto.setClarityOfBackground(1);
        dto.setContent(content);
        dto.setDataAnalysis(1);
        dto.setImportance(1);
        dto.setNoveltyOfConclusions(1);
        dto.setQualityOfPresentation(1);
        dto.setPublicationUri(publicationUri);
        dto.setStudyDesignAndMethods(1);
        
        ReflectionTestUtils.setField(service, "dao", dao);
        String uri = service.submitPeerReview(dto, USER_ID);
        
        assertThat(uri, containsString("scipub:review:"));
        verify(dao).persist(argument.capture());
        PeerReview entity = argument.getValue();
        assertThat(entity.getClarityOfBackground(), is(1));
        assertThat(entity.getDataAnalysis(), is(1));
        assertThat(entity.getNoveltyOfConclusions(), is(1));
        assertThat(entity.getImportance(), is(1));
        assertThat(entity.getQualityOfPresentation(), is(1));
        assertThat(entity.getStudyDesignAndMethods(), is(1));
        assertThat(entity.getCurrentRevision().getContent(), is(content));
        assertThat(entity.getPublication().getUri(), is(publicationUri));
    }
}
