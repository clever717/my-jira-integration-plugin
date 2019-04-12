package com.intellij.myjira.tasks;

import static com.intellij.myjira.rest.model.JiraPermission.EDIT_ISSUE;

import com.intellij.myjira.exceptions.InvalidPermissionException;
import com.intellij.myjira.exceptions.InvalidResultException;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ChangeIssuePriorityTask extends AbstractBackgroundableTask {

    private String priorityName;
    private String issueIdOrKey;

    public ChangeIssuePriorityTask(@NotNull Project project, String priorityName, String issueIdOrKey) {
        super(project, "Updating Issue Priority...");
        this.priorityName = priorityName;
        this.issueIdOrKey = issueIdOrKey;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        // Check user permissions
        boolean hasPermission = jiraRestApi.userHasPermissionOnIssue(issueIdOrKey, EDIT_ISSUE);
        if(!hasPermission){
            throw new InvalidPermissionException("Jira", "You don't have permission to change priority");
        }

        Result result = jiraRestApi.changeIssuePriority(priorityName, issueIdOrKey);
        if(!result.isValid()){
            throw new InvalidResultException("Error", "Issue priority has not been updated");
        }

        // Retrieve updated issue
        Result issueResult = jiraRestApi.getIssue(issueIdOrKey);
        if(issueResult.isValid()){
            JiraIssue issue = (JiraIssue) issueResult.get();
            // Update panels
            getJiraIssueUpdater().update(issue);
        }

    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Issue priority updated");
    }

}
