package com.intellij.myjira.actions;

import static java.util.Objects.isNull;

import com.intellij.myjira.tasks.ChangeIssuePriorityTask;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class JiraIssueChangePriorityAction extends JiraIssueAction {

    private String priorityName;
    private String issueIdOrKey;

    public JiraIssueChangePriorityAction(String priorityName, String issueIdOrKey) {
        super(ActionProperties.of(priorityName));
        this.priorityName = priorityName;
        this.issueIdOrKey = issueIdOrKey;
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        new ChangeIssuePriorityTask(project, priorityName, issueIdOrKey).queue();
    }

}
