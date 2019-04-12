package com.intellij.myjira.tasks;

import com.intellij.myjira.exceptions.InvalidResultException;
import com.intellij.myjira.helper.TransitionFieldHelper.FieldEditorInfo;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.util.Result;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.project.Project;
import java.util.Map;
import org.jetbrains.annotations.NotNull;

public class TransitIssueTask extends AbstractBackgroundableTask {

    private String issueId;
    private String transitionId;
    private Map<String, FieldEditorInfo> fields;

    public TransitIssueTask(@NotNull Project project, String issueId, String transitionId, Map<String, FieldEditorInfo> transitionFields) {
        super(project, "Transiting Issue...");
        this.issueId = issueId;
        this.transitionId = transitionId;
        this.fields = transitionFields;
    }

    @Override
    public void run(@NotNull ProgressIndicator indicator) {
        JiraRestApi jiraRestApi = getJiraRestApi();
        Result result = jiraRestApi.transitIssue(issueId, transitionId, fields);
        if(!result.isValid()) {
            throw new InvalidResultException("Transition error", "Issue has not been updated");
        }

        // Retrieve updated issue
        Result issueResult = jiraRestApi.getIssue(issueId);
        if(issueResult.isValid()){
            JiraIssue issue = (JiraIssue) issueResult.get();
            // Update panels
            getJiraIssueUpdater().update(issue);
        }

    }


    @Override
    public void onSuccess() {
        showNotification("Transition successful", "Issue status has been updated");
    }

}
