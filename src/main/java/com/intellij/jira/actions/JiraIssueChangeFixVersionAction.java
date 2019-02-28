package com.intellij.jira.actions;

import com.intellij.jira.tasks.ChangeIssueFixVersionTask;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

import static java.util.Objects.isNull;

public class JiraIssueChangeFixVersionAction extends JiraIssueAction {

    private String id;
    private String version;
    private String issueIdOrKey;

    public JiraIssueChangeFixVersionAction(String version, String id, String issueIdOrKey) {
        super(ActionProperties.of(version));
        this.id = id;
        this.version = version;
        this.issueIdOrKey = issueIdOrKey;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (isNull(project)) {
            return;
        }

        new ChangeIssueFixVersionTask(project, version, issueIdOrKey).queue();
    }

}
