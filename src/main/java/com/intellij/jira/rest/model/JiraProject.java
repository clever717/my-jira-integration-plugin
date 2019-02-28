package com.intellij.jira.rest.model;

import java.util.List;
import java.util.Objects;

public class JiraProject {

    private String id;
    private String name;
    private String self;
    private String key;
    private List<JiraIssueType> issueTypes;

    public JiraProject() { }

    public JiraProject(String id) {
        this.id = id;
    }

    public JiraProject(String id, String name, String self, String key, List<JiraIssueType> issueTypes) {
        this.id = id;
        this.name = name;
        this.self = self;
        this.key = key;
        this.issueTypes = issueTypes;
    }

    public JiraProject(JiraProject other) {
        this(other.getId(), other.getSelf(), other.getName(), other.getKey(), other.getIssueTypes());
    }

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

    @Override
    public JiraProject clone() {
        return new JiraProject(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraProject that = (JiraProject) o;
        return Objects.equals(id, that.id);
    }
}
