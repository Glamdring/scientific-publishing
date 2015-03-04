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
}
