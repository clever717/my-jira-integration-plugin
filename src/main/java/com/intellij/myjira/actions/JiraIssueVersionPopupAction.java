package com.intellij.myjira.actions;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraProjectVersionDetails;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.popup.JiraIssueVersionPopup;
import com.intellij.myjira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.List;
import java.util.stream.Collectors;

public class JiraIssueVersionPopupAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Version", AllIcons.Vcs.History);
    private JiraIssueFactory issueFactory;

    public JiraIssueVersionPopupAction(JiraIssueFactory factory) {
        super(properties);
        this.issueFactory = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (isNull(project)) {
            return;
        }

        JiraServerManager manager = project.getComponent(JiraServerManager.class);
        JiraRestApi jiraRestApi = manager.getJiraRestApi();
        if (isNull(jiraRestApi)) {
            return;
        }

        JiraIssue issue = this.issueFactory.create();
        List<JiraProjectVersionDetails> versions = jiraRestApi.getProjectVersionDetails(issue.getProject().getKey());
      versions = versions.stream().filter(v -> !v.isReleased()).collect(Collectors.toList());
        JiraIssueVersionPopup popup = new JiraIssueVersionPopup(createActionGroup(versions, issue), project);
        popup.showInCenterOf(getComponent());

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }

    private ActionGroup createActionGroup(List<JiraProjectVersionDetails> versions, JiraIssue issue) {
        JiraIssueActionGroup group = new JiraIssueActionGroup(getComponent());
        versions.forEach(p -> group.add(new JiraIssueChangeFixVersionAction(p.getName(), p.getId(), issue.getKey())));

        return group;
    }
}
