package com.intellij.myjira.tasks;

import static java.util.Objects.nonNull;

import com.intellij.myjira.components.JQLSearcherManager;
import com.intellij.myjira.components.JiraNotificationManager;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.jql.JQLSearcher;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.notification.Notifications;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class RefreshIssuesTask extends AbstractBackgroundableTask {

    public RefreshIssuesTask(@NotNull Project project) {
        super(project, "Updating Issues from Server");
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {

        JiraRestApi jiraRestApi = myProject.getComponent(JiraServerManager.class).getJiraRestApi();

        JQLSearcherManager jqlSearcherManager = myProject.getComponent(JQLSearcherManager.class);
        JQLSearcher searcher = jqlSearcherManager.getSelectedSearcher();

        List<JiraIssue> issues = new ArrayList<>();
        if(nonNull(searcher) && nonNull(jiraRestApi)){
            issues =  jiraRestApi.getIssues(searcher.getJql());
        }

        getJiraIssueUpdater().update(issues);
    }

    @Override
    public void showNotification(String title, String content) {
        Notifications.Bus.notify(JiraNotificationManager.getInstance().createSilentNotification(title, content));
    }

    @Override
    public void onSuccess() {
        showNotification("Jira", "Issues are now up to date");
    }

}
