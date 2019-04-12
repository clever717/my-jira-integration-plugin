package com.intellij.myjira.actions;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssueForCreate;
import com.intellij.myjira.rest.model.JiraIssueUser;
import com.intellij.myjira.rest.model.JiraProject;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.dialog.IssueCreateDialog;
import com.intellij.myjira.util.JiraIssueFactory;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.awt.Cursor;
import java.util.List;
import javax.swing.SwingUtilities;

public class AddJiraSubIssueDialogAction extends JiraIssueAction {
    private static final ActionProperties properties = ActionProperties.of("SubIssue", AllIcons.General.CopyHovered);

    private JiraIssueFactory issueFactory;


    public AddJiraSubIssueDialogAction(JiraIssueFactory factory) {
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
            getComponent().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingUtilities.invokeLater(() -> {
                JiraIssue issue = issueFactory.create();
                JiraIssueUser myInfo = jiraRestApi.getMyInfo();
                List<JiraProject> projectList = jiraRestApi.getProjects();
                List<JiraIssueUser> assignableUsers = jiraRestApi.getAssignableUsers("issue", issue.getKey());

                JiraIssueForCreate createModel = new JiraIssueForCreate();

                //내 정보
                createModel.setMyInfo(myInfo);
                //모든 사용자
                createModel.setAssignableUserList(assignableUsers);
                //모든 프로젝트
                createModel.setProjectList(projectList);

                /*부모 이슈 내용 세팅*/

                //부모 키
                createModel.setParentIssueIdOrKey(issue.getKey());
                //프로젝트
                createModel.setProjectId(issue.getProject().getId());
                //요약
                createModel.setSummary(issue.getSummary());
                //보고자
                createModel.setReporter(myInfo.getName());
                //담당자
                createModel.setAssignee(myInfo.getName());
                //내용
                createModel.setDescription(issue.getDescription());


                IssueCreateDialog dialog = new IssueCreateDialog(project, createModel);
                dialog.show();
                getComponent().setCursor(Cursor.getDefaultCursor());
            });

        }

    }

    @Override
    public void update(AnActionEvent e) {
        e.getPresentation().setEnabled(nonNull(issueFactory.create()));
    }


}
