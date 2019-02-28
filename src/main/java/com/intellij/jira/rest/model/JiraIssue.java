package com.intellij.jira.rest.model;

import com.intellij.jira.rest.JiraIssueCommentsWrapper;
import com.intellij.util.containers.ContainerUtil;

import java.util.Date;
import java.util.List;
import java.util.Objects;

public class JiraIssue {

    public static final String REQUIRED_FIELDS = "summary,description,created,updated,duedate,resolutiondate,assignee,reporter,issuetype,status,priority,comment,issuelinks,project,versions,fixVersions";

    private String id;
    private String self;
    private String key;
    private JiraIssue.Fields fields;

    public JiraIssue() { }

    public String getId() {
        return id;
    }

    public String getSelf() {
        return self;
    }

    public String getKey() {
        return key;
    }

    public String getSummary() {
        return fields.summary;
    }

    public String getDescription() {
        return fields.description;
    }

    public Date getCreated() {
        return fields.created;
    }

    public Date getUpdated() {
        return fields.updated;
    }

    public Date getResolutiondate() {
        return fields.resolutiondate;
    }

    public Date getDuedate() {
        return fields.duedate;
    }

    public JiraIssueType getIssuetype() {
        return fields.issuetype;
    }

    public JiraIssueStatus getStatus() {
        return fields.status;
    }

    public JiraIssuePriority getPriority() {
        return fields.priority;
    }

    public JiraIssueUser getAssignee() {
        return fields.assignee;
    }

    public JiraIssueUser getCreator() {
        return fields.creator;
    }

    public JiraIssueUser getReporter() {
        return fields.reporter;
    }

    public JiraIssueCommentsWrapper getComments(){
        return fields.comment;
    }

    public List<JiraIssueLink> getIssueLinks(){
        return fields.issuelinks;
    }

    public JiraProject getProject(){
        return fields.project;
    }

    public List<JiraProjectVersion> getVersions(){
        return fields.versions;
    }


    public List<JiraProjectVersionDetails> getFixVersions() {
        return fields.fixVersions;
    }

    public String getUrl(){
        return self.replaceFirst("(/rest([\\w/]+))", "/browse/" + getKey());
    }

    public boolean isResolved(){
        return getStatus().isDoneCategory();
    }

    public static class Fields{

        private String summary;
        private String description;
        private Date created;
        private Date updated;
        private Date resolutiondate;
        private Date duedate;
        private JiraIssueType issuetype;
        private JiraIssueStatus status;
        private JiraIssuePriority priority;
        private JiraIssueUser assignee;
        private JiraIssueUser creator;
        private JiraIssueUser reporter;
        private JiraIssueCommentsWrapper comment;
        private List<JiraIssueLink> issuelinks = ContainerUtil.emptyList();
        private JiraProject project;
        private List<JiraProjectVersion> versions = ContainerUtil.emptyList();
        private List<JiraProjectVersionDetails> fixVersions = ContainerUtil.emptyList();

        public Fields() { }

        public String getSummary() {
            return summary;
        }

        public void setSummary(String summary) {
            this.summary = summary;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Date getCreated() {
            return created;
        }

        public void setCreated(Date created) {
            this.created = created;
        }

        public Date getUpdated() {
            return updated;
        }

        public void setUpdated(Date updated) {
            this.updated = updated;
        }

        public Date getResolutiondate() {
            return resolutiondate;
        }

        public void setResolutiondate(Date resolutiondate) {
            this.resolutiondate = resolutiondate;
        }

        public Date getDuedate() {
            return duedate;
        }

        public void setDuedate(Date duedate) {
            this.duedate = duedate;
        }

        public JiraIssueType getIssuetype() {
            return issuetype;
        }

        public void setIssuetype(JiraIssueType issuetype) {
            this.issuetype = issuetype;
        }

        public JiraIssueStatus getStatus() {
            return status;
        }

        public void setStatus(JiraIssueStatus status) {
            this.status = status;
        }

        public JiraIssuePriority getPriority() {
            return priority;
        }

        public void setPriority(JiraIssuePriority priority) {
            this.priority = priority;
        }

        public JiraIssueUser getAssignee() {
            return assignee;
        }

        public void setAssignee(JiraIssueUser assignee) {
            this.assignee = assignee;
        }

        public JiraIssueUser getCreator() {
            return creator;
        }

        public void setCreator(JiraIssueUser creator) {
            this.creator = creator;
        }

        public JiraIssueUser getReporter() {
            return reporter;
        }

        public void setReporter(JiraIssueUser reporter) {
            this.reporter = reporter;
        }

        public JiraIssueCommentsWrapper getComment() {
            return comment;
        }

        public void setComment(JiraIssueCommentsWrapper comment) {
            this.comment = comment;
        }

        public List<JiraIssueLink> getIssuelinks() {
            return issuelinks;
        }

        public void setIssuelinks(List<JiraIssueLink> issuelinks) {
            this.issuelinks = issuelinks;
        }

        public JiraProject getProject() {
            return project;
        }

        public void setProject(JiraProject project) {
            this.project = project;
        }

        public List<JiraProjectVersion> getVersions() {
            return versions;
        }

        public void setVersions(List<JiraProjectVersion> versions) {
            this.versions = versions;
        }

        public List<JiraProjectVersionDetails> getFixVersions() {
            return fixVersions;
        }

        public void setFixVersions(List<JiraProjectVersionDetails> fixVersions) {
            this.fixVersions = fixVersions;
        }
    }

    public Fields getFields() {
        return fields;
    }

    public void setFields(Fields fields) {
        this.fields = fields;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraIssue jiraIssue = (JiraIssue) o;
        return Objects.equals(id, jiraIssue.id) &&
                Objects.equals(key, jiraIssue.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, key);
    }
}
