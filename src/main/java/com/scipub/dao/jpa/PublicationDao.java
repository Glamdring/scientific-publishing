package com.scipub.dao.jpa;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;
import com.scipub.model.PublicationSource;

@Repository
public class PublicationDao extends Dao {

    public List<PublicationRevision> getRevisions(Publication paper) {
        QueryDetails<PublicationRevision> query = new QueryDetails<PublicationRevision>().setQueryName("Publication.getRevisions")
            .setParamNames("paper")
            .setParamValues(paper)
            .setResultClass(PublicationRevision.class);
        
        return findByQuery(query);
    }

    public List<Publication> getLatestPapers(long branchId, int papersPerBranch) {
        QueryDetails<Publication> query = new QueryDetails<Publication>().setQueryName("Publication.getLatestByBranch")
                .setParamNames("branchId")
                .setParamValues(branchId)
                .setCount(papersPerBranch)
                .setResultClass(Publication.class);
            
        return findByQuery(query);
    }

    public void storePublications(List<Publication> publications) {
        getEntityManager().setFlushMode(FlushModeType.COMMIT);
        for (Publication publication : publications) {
            persist(publication);
        }
    }

    public LocalDateTime getLastImportDate(PublicationSource source) {
        QueryDetails<LocalDateTime> query = new QueryDetails<LocalDateTime>()
                .setQuery("SELECT p.created FROM Publication p WHERE p.source=:source ORDER BY created DESC")
                .setCount(1)
                .setParamNames("source")
                .setParamValues(source)
                .setResultClass(LocalDateTime.class);
        
        return getSingleResult(findByQuery(query));
        
    }
}
