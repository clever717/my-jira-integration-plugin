package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueForCreate;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CreateIssueTask extends AbstractBackgroundableTask {

    private JiraIssueForCreate issue;

    public CreateIssueTask(@NotNull Project project, JiraIssueForCreate issue) {
        super(project, "Create Issue...");
        this.issue = issue;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        Result result = jiraRestApi.createIssue(issue);
        if (!result.isValid()) {
            throw new InvalidResultException("Create error", "Issue has not been created");
        }

        //부모 이슈에서 만들때만 링크 자동 연결
        if (issue.getParentIssueIdOrKey() != null) {
            JiraIssue createdIssue = (JiraIssue) result.get();
            Result linked = jiraRestApi.linkIssue(createdIssue.getKey(), issue.getParentIssueIdOrKey());
        }
    }


    @Override
    public void onSuccess() {
        showNotification("Create successful", "Issue has been created");
    }

}
