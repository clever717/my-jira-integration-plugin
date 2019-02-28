package com.intellij.jira.rest.model;

public class JiraIssueType {

    private String id;
    private String self;
    private String name;
    private String description;
    private String iconUrl;
    private boolean subtask;

    public JiraIssueType() { }

    public JiraIssueType(String id, String self, String name, String description, String iconUrl, boolean subtask) {
        this.id = id;
        this.self = self;
        this.name = name;
        this.description = description;
        this.iconUrl = iconUrl;
        this.subtask = subtask;
    }

    public JiraIssueType(JiraIssueType other) {
        this(other.getId(), other.getSelf(), other.getName(), other.getDescription(), other.getIconUrl(), other.isSubtask());
    }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconUrl() {
        return iconUrl;
    }

    public boolean isSubtask() {
        return subtask;
    }

    @Override
    public JiraIssueType clone() {
        return new JiraIssueType(this);
    }

    @Override
    public String toString() {
        return name;
    }
}
