package com.intellij.myjira.actions;

import static java.util.Objects.nonNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.rest.model.JiraIssueUser;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.popup.JiraIssueAssignableUsersPopup;
import com.intellij.myjira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.List;

public class JiraIssueAssigneePopupAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Assign",  AllIcons.General.User);

    private JiraIssueFactory issueFactory;

    public JiraIssueAssigneePopupAction(JiraIssueFactory factory) {
        super(properties);
        this.issueFactory = factory;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)) {
            JiraServerManager jiraServerManager = project.getComponent(JiraServerManager.class);
            JiraRestApi jiraServer = jiraServerManager.getJiraRestApi();
            if(nonNull(jiraServer)){
                List<JiraIssueUser> assignableUsers = jiraServer.getAssignableUsers("issue", issueFactory.create().getKey());
                JiraIssueAssignableUsersPopup popup = new JiraIssueAssignableUsersPopup(createActionGroup(assignableUsers), project);
                popup.showInCenterOf(getComponent());
            }
        }
    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }

    private ActionGroup createActionGroup(List<JiraIssueUser> assignableUsers){
        JiraIssueActionGroup group = new JiraIssueActionGroup(getComponent());
        assignableUsers.forEach(u -> group.add(JiraIssueAssignmentExecuteAction.assignUser(u.getKey(), issueFactory.create().getKey())));
        group.add(JiraIssueAssignmentExecuteAction.assignAnyone(issueFactory.create().getKey()));

        return group;
    }

}
