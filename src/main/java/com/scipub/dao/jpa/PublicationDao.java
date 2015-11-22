package com.scipub.dao.jpa;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.FlushModeType;

import org.springframework.stereotype.Repository;

import com.scipub.model.Branch;
import com.scipub.model.Publication;
import com.scipub.model.PublicationRevision;
import com.scipub.model.PublicationSource;
import com.scipub.model.User;

@Repository
public class PublicationDao extends Dao {

    @Inject
    private PeerReviewDao peerReviewDao;
    
    public List<PublicationRevision> getRevisions(Publication publication) {
        QueryDetails<PublicationRevision> query = new QueryDetails<PublicationRevision>().setQueryName("Publication.getRevisions")
            .setParamNames("publication")
            .setParamValues(publication)
            .setResultClass(PublicationRevision.class);
        
        return findByQuery(query);
    }

    public List<Publication> getLatestPapers(Branch branch, int publicationsPerBranch) {
        QueryDetails<Publication> query = new QueryDetails<Publication>().setQueryName("Publication.getLatestByBranch")
                .setParamNames("branch")
                .setParamValues(branch)
                .setCount(publicationsPerBranch)
                .setResultClass(Publication.class);
            
        return findByQuery(query);
    }

    public void storePublications(List<Publication> publications) {
        getEntityManager().setFlushMode(FlushModeType.COMMIT);
        for (Publication publication : publications) {
            persist(publication);
        }
    }
    
    public List<Publication> getPublicationsByUser(User user) {
        QueryDetails<Publication> query = new QueryDetails<>();
        
        query.setQueryName("Publication.getPublicationsOfUser")
             .setParamNames(new String[] {"user"})
             .setParamValues(new Object[] {user})
             .setResultClass(Publication.class);
        
        return findByQuery(query);
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
    
    public List<PublicationRevision> getAllRevisions(Publication publication) {
        return getListByPropertyValue(PublicationRevision.class, "publication", publication);
    }
    

    public void deletePublications(User user) {
        List<Publication> publications = getPublicationsByUser(user);
        // delete only the ones where the user is a single registered author
        publications.stream().filter(p -> p.getAuthors().size() == 1).forEach(p -> {
            if (p.getCurrentRevision() != null) {
                p.setCurrentRevision(null);
            }
            // delete all the revisions
            getAllRevisions(p).forEach(r -> delete(r));
            // delete all associated peer reviews (even those by other authors) TODO maybe just mark them as deleted/orhaned?
            peerReviewDao.deletePeerReviewsForPublication(p);
            peerReviewDao.deletePreliminaryReviewsForPublication(p);
            delete(p);
        });
    }
}
