package com.scipub.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;

@Repository
public class PaperDao extends Dao {

    public List<PublicationRevision> getRevisions(Publication paper) {
        QueryDetails<PublicationRevision> query = new QueryDetails<PublicationRevision>().setQueryName("Paper.getRevisions")
            .setParamNames(new String[] {"paper"})
            .setParamValues(new Object[] {paper})
            .setResultClass(PublicationRevision.class);
        
        return findByQuery(query);
    }

    public List<Publication> getLatestPapers(long branchId, int papersPerBranch) {
        QueryDetails<Publication> query = new QueryDetails<Publication>().setQueryName("Paper.getLatestByBranch")
                .setParamNames(new String[] {"branchId"})
                .setParamValues(new Object[] {branchId})
                .setCount(papersPerBranch)
                .setResultClass(Publication.class);
            
        return findByQuery(query);
    }
}
