package com.scipub.tools;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

public class BranchExtractor {

    public static void main(String[] args) throws Exception {
        List<Branch> branches = extractBranches();
        for (Branch branch : branches) {
            System.out.println("INSERT INTO `branch` VALUES (" + branch.getId() + ",\""
                    + branch.name.replace("\"", "'") + "\"," + getParentId(branch) + ");");
        }
    }

    private static String getParentId(Branch branch) {
        if (branch.getParentId() != 0) {
            return String.valueOf(branch.getParentId());
        } else {
            return "null";
        }
    }

    public static List<Branch> extractBranches() throws IOException {
        InputStream in = BranchExtractor.class.getResourceAsStream("/sql/sciences.txt");
        List<String> lines = CharStreams.readLines(new InputStreamReader(in, Charsets.UTF_8));
        
        Pattern p = Pattern.compile("\\[\\[([^\\[\\]]+)\\]\\]");
        Stack<Branch> stack = new Stack<Branch>();
        List<Branch> branches = new ArrayList<>();
        int id = 1;
        for (String line : lines) {
            int level;
            if (line.startsWith("*")) {
                String pref = line.split(" ")[0];
                level = pref.length();
            } else {
                level = 0;
            }
            
            Matcher m = p.matcher(line);
            if (m.find()) {
                String name = m.group(1);
                Branch branch = new Branch();
                branch.setName(name);
                branch.setId(id);

                while (stack.size() > level) {
                    stack.pop();
                }
                if (!stack.isEmpty()) {
                    branch.setParentId(stack.peek().getId());
                }
                stack.push(branch);
                branches.add(branch);
                id++;
                
                
            } else {
                throw new IllegalStateException("Input file is not well formed at: " + line);
            }
        }
        return branches;
    }
    
    public static class Branch {
        private int id;
        private String name;
        private int parentId;
        public int getId() {
            return id;
        }
        public void setId(int id) {
            this.id = id;
        }
        public String getName() {
            return name;
        }
        public void setName(String name) {
            this.name = name;
        }
        public int getParentId() {
            return parentId;
        }
        public void setParentId(int parentId) {
            this.parentId = parentId;
        }
        @Override
        public String toString() {
            return "Branch [id=" + id + ", name=" + name + ", parentId=" + parentId + "]";
        }
    }
}
