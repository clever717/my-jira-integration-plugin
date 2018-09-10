package com.intellij.jira.tasks;

import com.intellij.jira.rest.JiraRestClient;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueComment;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.jira.rest.model.JiraIssueUser;
import com.intellij.jira.util.BodyResult;
import com.intellij.jira.util.EmptyResult;
import com.intellij.jira.util.Result;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.util.containers.ContainerUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class JiraServer {

    private static final Logger log = LoggerFactory.getLogger(JiraServer.class);

    private JiraRestClient jiraRestClient;

    public JiraServer(JiraRepository jiraRepository) {
        this.jiraRestClient = new JiraRestClient(jiraRepository);
    }


    public JiraIssue getIssue(String issueIdOrKey){
        try {
            return this.jiraRestClient.getIssue(issueIdOrKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // TODO: poner Result
        return null;
    }


    public List<JiraIssue> getIssues() {
        try {
            return this.jiraRestClient.findIssues();
        } catch (Exception e) {
            log.error("No issues found");
            return ContainerUtil.emptyList();
        }
    }


    public List<JiraIssueTransition> getTransitions(String issueId){
        try {
            return jiraRestClient.getTransitions(issueId);
        } catch (Exception e) {
            log.error(String.format("No transitions was found for issue '%s'", issueId));
            return ContainerUtil.emptyList();
        }
    }


    public Result transitIssue(String issueId, String transitionId){
        try {
            String response = jiraRestClient.transitIssue(issueId, transitionId);
            return EmptyResult.create(response);
        } catch (Exception e) {
            log.error(String.format("Error executing transition '%s' in issue '%s'", transitionId, issueId));
            return EmptyResult.error();
        }
    }

    public List<JiraIssueUser> getAssignableUsers(String issueKey){
        try {
            return jiraRestClient.getAssignableUsers(issueKey);
        } catch (Exception e) {
            log.error("Error fetching users to assign");
            return ContainerUtil.emptyList();
        }
    }


    public Result assignUserToIssue(String username, String issueKey){
        try {
            String response = jiraRestClient.assignUserToIssue(username, issueKey);
            return EmptyResult.create(response);
        } catch (Exception e) {
            log.error(String.format("Error assigning user '%s' to issue '%s'", username, issueKey));
            return EmptyResult.error();
        }
    }


    public Result addCommentToIssue(String body, String issueKey){
        try {
            JiraIssueComment comment = jiraRestClient.addCommentToIssue(body, issueKey);
            return BodyResult.ok(comment);
        } catch (Exception e) {
            log.error(String.format("Error creating comment in issue '%s'", issueKey));
            return BodyResult.error();
        }
    }


    public Result deleteCommentToIssue(String issueKey, String commentId) {
        try {
            String response = jiraRestClient.deleteCommentToIssue(issueKey, commentId);
            return EmptyResult.create(response);
        } catch (Exception e) {
            log.error(String.format("Error deleting comment in issue '%s'", issueKey));
            return EmptyResult.error();
        }

    }
}
