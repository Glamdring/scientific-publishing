package com.scipub.tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.scipub.tools.BranchExtractor.Branch;

public class BranchJsonGenerator {
    
    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Branch> branches = BranchExtractor.extractBranches();
        Map<Integer, ScienceBranch> branchesMap = new HashMap<>(); 
        for (Branch branch : branches) {
            ScienceBranch sciBranch = new ScienceBranch();
            sciBranch.setName(branch.getName());
            sciBranch.setId(branch.getId());
            branchesMap.put(branch.getId(), sciBranch);
        }
        for (Branch branch : branches) {
            if (branch.getParentId() != 0) {
                branchesMap.get(branch.getParentId()).getChildren().add(branchesMap.get(branch.getId()));
            }
        }
        
        List<ScienceBranch> roots = new ArrayList<>();
        for (Branch branch : branches) {
            if (branch.getParentId() == 0) {
                roots.add(branchesMap.get(branch.getId()));
            }
        }
        
        ScienceBranch root = new ScienceBranch();
        root.setName("Science");
        root.setChildren(roots);
        System.out.println(mapper.writeValueAsString(root));
    }
    
    public static class ScienceBranch {
        private String name;
        private int id;
        private List<ScienceBranch> children = new ArrayList<>();
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public List<ScienceBranch> getChildren() {
            return children;
        }
        public void setChildren(List<ScienceBranch> children) {
            this.children = children;
        }
    }
}
