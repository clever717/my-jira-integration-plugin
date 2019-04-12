package com.intellij.myjira.tasks;

import static com.intellij.myjira.rest.model.JiraPermission.COMMENT_DELETE_OWN;

import com.intellij.myjira.exceptions.InvalidPermissionException;
import com.intellij.myjira.exceptions.InvalidResultException;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class DeleteCommentTask extends AbstractBackgroundableTask {
    private String issueKey;
    private String commentId;

    public DeleteCommentTask(@NotNull Project project, String issueKey, String commentId) {
        super(project, "Deleting comment...");
        this.issueKey = issueKey;
        this.commentId = commentId;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        // Check user permissions
        boolean hasPermission = jiraRestApi.userHasPermissionOnIssue(issueKey, COMMENT_DELETE_OWN);
        if(!hasPermission){
            throw new InvalidPermissionException("Jira", "You don't have permission to delete a comment");
        }

        Result result = jiraRestApi.deleteCommentToIssue(issueKey, commentId);
        if(!result.isValid()) {
            throw new InvalidResultException("Error", "Issue comment has not been deleted");
        }

        // Retrieve updated issue
        Result issueResult = jiraRestApi.getIssue(issueKey);
        if(issueResult.isValid()){
            JiraIssue issue = (JiraIssue) issueResult.get();
            // Update panels
            getJiraIssueUpdater().update(issue);
        }

    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Comment deleted successfully");
    }

}
