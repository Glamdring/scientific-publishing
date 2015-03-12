package com.scipub.dao.jpa;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.scipub.model.Branch;

@Repository
public class BranchDao extends Dao {

    public List<Branch> getTopLevelBranches() {
        return findByQuery(new QueryDetails<Branch>().setResultClass(Branch.class).setQuery(
                "SELECT b FROM Branch b WHERE b.parent IS NULL"));
    }
}
