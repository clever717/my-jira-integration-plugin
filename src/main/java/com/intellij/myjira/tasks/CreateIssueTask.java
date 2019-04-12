package com.intellij.myjira.tasks;

import static java.util.Objects.nonNull;

import com.intellij.myjira.exceptions.InvalidResultException;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssueForCreate;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CreateIssueTask extends AbstractBackgroundableTask {

    private JiraIssueForCreate issue;
    private Project project;

    public CreateIssueTask(@NotNull Project project, JiraIssueForCreate issue) {
        super(project, "Create Issue...");
        this.project = project;
        this.issue = issue;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        Result result = jiraRestApi.createIssue(issue);
        if (!result.isValid()) {
            throw new InvalidResultException("Create error", "Issue has not been created");
        }
        JiraIssue createdIssue = (JiraIssue) result.get();
        //부모 이슈에서 만들때만 링크 자동 연결
        if (nonNull(issue.getParentIssueIdOrKey()) && nonNull(createdIssue)) {
            Result linked = jiraRestApi.linkIssue(createdIssue.getKey(), issue.getParentIssueIdOrKey());
        }

        new RefreshIssuesTask(project).queue();
    }


    @Override
    public void onSuccess() {
        showNotification("Create successful", "Issue has been created");
    }

}
