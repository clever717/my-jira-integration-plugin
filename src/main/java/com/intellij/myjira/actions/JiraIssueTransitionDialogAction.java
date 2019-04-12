package com.intellij.myjira.actions;

import static java.util.Objects.nonNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssueTransition;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.dialog.IssueTransitionDialog;
import com.intellij.myjira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.List;

public class JiraIssueTransitionDialogAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Transit",  AllIcons.Actions.Forward);

    private JiraIssueFactory issueFactory;


    public JiraIssueTransitionDialogAction(JiraIssueFactory factory) {
        super(properties);
        this.issueFactory = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)) {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            JiraRestApi jiraRestApi = manager.getJiraRestApi();
            if(nonNull(jiraRestApi)){
                JiraIssue issue = issueFactory.create();
                List<JiraIssueTransition> transitions = jiraRestApi.getTransitions(issue.getId());

                IssueTransitionDialog dialog = new IssueTransitionDialog(project, issue, transitions);
                dialog.show();

            }
        }

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }





}
