package com.scipub.tools;

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
        InputStream in = BranchExtractor.class.getResourceAsStream("/sql/sciences.txt");
        List<String> lines = CharStreams.readLines(new InputStreamReader(in, Charsets.UTF_8));
        
        Pattern p = Pattern.compile("\\[\\[[^\\[\\]]+\\]\\]");
        Stack<Branch> stack = new Stack<Branch>();
        List<Branch> branches = new ArrayList<>();
        int id = 1;
        int currentLevel = 0;
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
                String name = m.group();
                Branch branch = new Branch();
                branch.setName(name);
                branch.setId(id);

                if (!stack.isEmpty()) {
                    branch.setParentId(stack.peek().getId());
                }
                
                if (level < stack.size()) {
                    stack.pop();
                } else if (level > stack.size()) {
                    stack.push(branch);
                }
                
                branches.add(branch);
                id++;
                
                
            } else {
                System.out.println("!!!!!!!!!!!!!!!!!!!!! " + line);
            }
        }
        for (Branch branch : branches) {
            System.out.println(branch);
        }
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
