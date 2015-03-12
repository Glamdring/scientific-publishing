package com.scipub.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scipub.model.Paper;
import com.scipub.model.PaperRevision;

@Repository
public class PaperDao extends Dao {

    public List<PaperRevision> getRevisions(Paper paper) {
        QueryDetails<PaperRevision> query = new QueryDetails<PaperRevision>().setQueryName("Paper.getRevisions")
            .setParamNames(new String[] {"paper"})
            .setParamValues(new Object[] {paper})
            .setResultClass(PaperRevision.class);
        
        return findByQuery(query);
    }

    public List<Paper> getLatestPapers(List<Long> branchIds) {
        QueryDetails<Paper> query = new QueryDetails<Paper>().setQueryName("Paper.getLatestByBranch")
                .setParamNames(new String[] {"branchIds"})
                .setParamValues(new Object[] {branchIds})
                .setResultClass(Paper.class);
            
            return findByQuery(query);
    }
}
