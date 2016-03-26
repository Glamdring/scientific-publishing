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
package com.scipub.tools;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scipub.tools.BranchExtractor.Branch;

public class BranchJsonGenerator {
    
    public static void main(String[] args) throws Exception {
        String json = getBranchJson(false);
        System.out.println(json);
    }

    public static String getBranchJson(boolean useSingleRoot) throws IOException, JsonProcessingException {
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
        
        if (useSingleRoot) {
            ScienceBranch root = new ScienceBranch();
            root.setName("Science");
            root.setChildren(roots);
            return mapper.writeValueAsString(root);
        } else {
            return mapper.writeValueAsString(roots);
        }
        
    }
    
    public static void mainFlat(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        List<Branch> branches = BranchExtractor.extractBranches();
        System.out.println(mapper.writeValueAsString(branches));
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
