package com.intellij.jira.rest.model;

import java.util.List;

public class JiraIssueForCreate {

    private String summary;
    private String reporter;
    private String assignee;
    private String description;
    private String projectId;
    private String issueTypeId;
    private String parentIssueIdOrKey;

    private JiraIssueUser myInfo;
    private List<JiraProject> projectList;
    private List<JiraIssueUser> assignableUserList;

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAssignee() {
        return assignee;
    }

    public void setAssignee(String assignee) {
        this.assignee = assignee;
    }

    public String getReporter() {
        return reporter;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProjectId() {
        return projectId;
    }

    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    public String getIssueTypeId() {
        return issueTypeId;
    }

    public void setIssueTypeId(String issueTypeId) {
        this.issueTypeId = issueTypeId;
    }

    public String getParentIssueIdOrKey() {
        return parentIssueIdOrKey;
    }

    public void setParentIssueIdOrKey(String parentIssueIdOrKey) {
        this.parentIssueIdOrKey = parentIssueIdOrKey;
    }

    public List<JiraProject> getProjectList() {
        return projectList;
    }

    public void setProjectList(List<JiraProject> projectList) {
        this.projectList = projectList;
    }

    public List<JiraIssueUser> getAssignableUserList() {
        return assignableUserList;
    }

    public void setAssignableUserList(List<JiraIssueUser> assignableUserList) {
        this.assignableUserList = assignableUserList;
    }

    public JiraIssueUser getMyInfo() {
        return myInfo;
    }

    public void setMyInfo(JiraIssueUser myInfo) {
        this.myInfo = myInfo;
    }
}
