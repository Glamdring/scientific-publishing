package com.scipub.scheduled;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.InputStream;
import java.time.LocalDate;
import java.util.List;

import org.apache.commons.lang.math.RandomUtils;
import org.junit.Test;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.springframework.test.util.ReflectionTestUtils;

import com.scipub.dao.jpa.BranchDao;
import com.scipub.model.Branch;
import com.scipub.model.Publication;

public class ArxivImporterTest {

    @Test
    public void testImport() throws Exception {
        ArxivImporter importer = new ArxivImporter();
        BranchDao dao = mock(BranchDao.class);
        when(dao.getByPropertyValue(any(), any(), any())).thenAnswer(new Answer<Branch>() {
            @Override
            public Branch answer(InvocationOnMock invocation) throws Throwable {
                String branchName = (String) invocation.getArguments()[2];
                Branch branch = new Branch();
                branch.setId(RandomUtils.nextLong());
                branch.setName(branchName);
                return branch;
            }
        });
        
        ReflectionTestUtils.setField(importer, "dao", dao);
        
        InputStream apiResponse = ArxivImporterTest.class.getResourceAsStream("/arxiv/api-response.xml");
        
        List<Publication> publications = importer.extractPublications(apiResponse, 2);
        
        Publication first = publications.get(0);
        
        assertThat(first.getUri(), equalTo("http://arxiv.org/abs/1503.07585v1"));
        assertThat(first.getCurrentRevision().getTitle(), equalTo("Internal algebra classifiers as codescent objects of crossed internal categories"));
        assertThat(first.getCurrentRevision().getPublicationAbstract(), containsString("these universal objects must be constructed"));
        assertThat(first.getCurrentRevision().getContentLink(), equalTo("http://arxiv.org/pdf/1503.07585v1"));
        assertThat(first.getPrimaryBranch().getName(), equalTo("Category theory"));
        assertThat(first.getBranches(), hasSize(3));
        assertThat(first.getNonRegisteredAuthors(), hasSize(1));
        assertThat(first.getNonRegisteredAuthors().iterator().next(), equalTo("Mark Weber"));
        assertThat(first.getCreated().toLocalDate(), equalTo(LocalDate.of(2015, 3,  26)));
        
        Publication second = publications.get(1);
        
        assertThat(second.getUri(), equalTo("http://arxiv.org/abs/1503.07695v1"));
        assertThat(second.getCurrentRevision().getTitle(), equalTo("Symplectic fermions and a quasi-Hopf algebra structure on $\\bar{U}_i sl(2)$"));
        assertThat(second.getCurrentRevision().getPublicationAbstract(), containsString("of $\\bar{U}_i sl(2)$. We then give an explicit"));
        assertThat(second.getCurrentRevision().getContentLink(), equalTo("http://arxiv.org/pdf/1503.07695v1"));
        assertThat(second.getPrimaryBranch().getName(), equalTo("Quantum algebra"));
        assertThat(second.getBranches(), hasSize(4));
        assertThat(second.getNonRegisteredAuthors(), hasSize(2));
        assertThat(second.getNonRegisteredAuthors(), containsInAnyOrder("A. M. Gainutdinov", "I. Runkel"));
        assertThat(second.getCreated().toLocalDate(), equalTo(LocalDate.of(2015, 3,  26)));
    }
    
}
