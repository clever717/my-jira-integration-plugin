package com.intellij.myjira.actions;

import static java.util.Objects.isNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.rest.model.JiraProject;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.dialog.AddVersionDialog;
import com.intellij.myjira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.awt.Cursor;
import java.util.List;
import javax.swing.SwingUtilities;

public class AddJiraVersionDialogAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Version", AllIcons.Vcs.Patch);
    private JiraIssueFactory issueFactory;

    public AddJiraVersionDialogAction() {
        super(properties);
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
        getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingUtilities.invokeLater(() -> {
            List<JiraProject> projectList = jiraRestApi.getProjects();
            AddVersionDialog dialog = new AddVersionDialog(project, projectList);
            dialog.show();
            getComponent().setCursor(Cursor.getDefaultCursor());
        });


    }

    @Override
    public void update(AnActionEvent e) {
        Project project = e.getProject();
        if (isNull(project) || !project.isInitialized() || project.isDisposed()) {
            e.getPresentation().setEnabled(false);
        } else {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            if (manager.hasJiraServerConfigured()) {
                e.getPresentation().setEnabled(true);
            } else {
                e.getPresentation().setEnabled(false);
            }
        }
    }
}
