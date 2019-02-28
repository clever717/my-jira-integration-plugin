package com.intellij.jira.rest.model;

import java.util.List;

public class JiraProject {

    private String id;
    private String name;
    private String self;
    private String key;
    private List<JiraIssueType> issueTypes;

    public JiraProject() { }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSelf() {
        return self;
    }

    public String getKey() {
        return key;
    }

    public String getUrl(){
        return self.replaceFirst("(/rest([\\w/]+))", "/projects/" + getKey() + "/summary");
    }

    public List<JiraIssueType> getIssueTypes() {
        return issueTypes;
    }

    @Override
    public String toString() {
        return key;
    }
}
