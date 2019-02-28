package com.intellij.jira.actions;

import com.intellij.icons.AllIcons;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueUser;
import com.intellij.jira.rest.model.JiraProject;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.jira.ui.dialog.IssueCreateDialog;
import com.intellij.jira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import java.util.List;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

public class JiraSubIssueCreateDialogAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("SubIssue", AllIcons.General.CopyHovered);

    private JiraIssueFactory issueFactory;


    public JiraSubIssueCreateDialogAction(JiraIssueFactory factory) {
        super(properties);
        this.issueFactory = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (nonNull(project)) {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            JiraRestApi jiraRestApi = manager.getJiraRestApi();
            if (isNull(jiraRestApi)) {
                return;
            }
            JiraIssueUser myInfo = jiraRestApi.getMyInfo();
            JiraIssue issue = issueFactory.create();
            List<JiraIssueUser> assignableUsers = jiraRestApi.getAssignableUsers(issue.getKey());
            JiraProject jiraProject = (JiraProject) jiraRestApi.getProject(issue.getProject().getKey()).get();
            JiraIssue.Fields fields = new JiraIssue.Fields();
            //요약
            fields.setSummary(issue.getSummary());
            //보고자
            fields.setReporter(myInfo);
            //담당자
            fields.setAssignee(myInfo);
            //내용
            fields.setDescription(issue.getDescription());
            //프로젝트
            fields.setProject(jiraProject);

            IssueCreateDialog dialog = new IssueCreateDialog(project, fields, assignableUsers, issue.getKey());
            dialog.show();

        }

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }


}
