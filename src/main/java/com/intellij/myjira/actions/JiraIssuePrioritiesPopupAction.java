package com.intellij.myjira.actions;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssuePriority;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.popup.JiraIssuePrioritiesPopup;
import com.intellij.myjira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.List;

public class JiraIssuePrioritiesPopupAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Change priority",  AllIcons.Ide.UpDown);
    private JiraIssueFactory issueFactory;

    public JiraIssuePrioritiesPopupAction(JiraIssueFactory factory) {
        super(properties);
        this.issueFactory = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        JiraServerManager manager = project.getComponent(JiraServerManager.class);
        JiraRestApi jiraRestApi = manager.getJiraRestApi();
        if(isNull(jiraRestApi)){
           return;
        }

        JiraIssue issue = this.issueFactory.create();
        List<JiraIssuePriority> priorities = jiraRestApi.getIssuePriorities();

        JiraIssuePrioritiesPopup popup = new JiraIssuePrioritiesPopup(createActionGroup(priorities, issue), project);
        popup.showInCenterOf(getComponent());

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }

    private ActionGroup createActionGroup(List<JiraIssuePriority> priorities, JiraIssue issue) {
        JiraIssueActionGroup group = new JiraIssueActionGroup(getComponent());
        priorities.forEach(p -> group.add(new JiraIssueChangePriorityAction(p.getName(), issue.getKey())));

        return group;
    }
}
