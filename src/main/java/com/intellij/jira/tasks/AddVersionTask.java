package com.intellij.jira.tasks;

import com.intellij.jira.exceptions.InvalidResultException;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AddVersionTask extends AbstractBackgroundableTask {

    private String projectName;
    private String projectId;
    private String name;
    private String description;

    public AddVersionTask(@Nullable Project project, String projectName, String projectId, String name, String description) {
        super(project, "Adding a version");
        this.projectName = projectName;
        this.projectId = projectId;
        this.name = name;
        this.description = description;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();

        Result result = jiraRestApi.addVersionToProject(projectName, projectId, name, description);
        if (!result.isValid()) {
            throw new InvalidResultException("Error", "Project version has not been added");
        }

    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Project version added successfully");
    }


}
