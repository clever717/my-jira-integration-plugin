package com.intellij.myjira.actions;

import static java.util.Objects.isNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.rest.model.JiraIssueForCreate;
import com.intellij.myjira.rest.model.JiraIssueUser;
import com.intellij.myjira.rest.model.JiraProject;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.dialog.IssueCreateDialog;
import com.intellij.myjira.ui.panels.JiraIssuesPanel;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.awt.Cursor;
import java.util.List;
import javax.swing.SwingUtilities;

public class AddJiraIssueDialogAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("Issue", AllIcons.General.Add);

    public AddJiraIssueDialogAction() {
        super(properties);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if (isNull(project)) {
            return;
        }
        JiraIssuesPanel issuesPanel = (JiraIssuesPanel) getComponent();
        if (isNull(issuesPanel)) {
            return;
        }
        getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));

        SwingUtilities.invokeLater(() -> {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            JiraRestApi jiraRestApi = manager.getJiraRestApi();
            if (isNull(jiraRestApi)) {
                return;
            }
            JiraIssueUser myInfo = jiraRestApi.getMyInfo();
            List<JiraProject> projectList = jiraRestApi.getProjects();

            JiraIssueForCreate createModel = new JiraIssueForCreate();
            //내 정보
            createModel.setMyInfo(myInfo);
            //모든 사용자
            createModel.setAssignableUserList(null);
            //프로젝트
            createModel.setProjectList(projectList);

            //프로젝트
            createModel.setProjectId(null);
            //요약
            createModel.setSummary("");
            //보고자
            createModel.setReporter(myInfo.getName());
            //담당자
            createModel.setAssignee(null);
            //내용
            createModel.setDescription(null);


            IssueCreateDialog dialog = new IssueCreateDialog(project, createModel);
            dialog.show();

            getComponent().setCursor(Cursor.getDefaultCursor());
        });
    }

    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        if (isNull(project) || !project.isInitialized() || project.isDisposed()) {
            event.getPresentation().setEnabled(false);
        } else {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            if (manager.hasJiraServerConfigured()) {
                event.getPresentation().setEnabled(true);
            } else {
                event.getPresentation().setEnabled(false);
            }
        }
    }


}
