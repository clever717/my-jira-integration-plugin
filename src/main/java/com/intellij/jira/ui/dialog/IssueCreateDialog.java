package com.intellij.jira.ui.dialog;

import com.intellij.jira.rest.model.JiraIssueForCreate;
import com.intellij.jira.rest.model.JiraIssueType;
import com.intellij.jira.rest.model.JiraIssueUser;
import com.intellij.jira.rest.model.JiraProject;
import com.intellij.jira.server.JiraRestApi;
import com.intellij.jira.server.JiraServerManager;
import com.intellij.jira.tasks.CreateIssueTask;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextArea;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.intellij.jira.util.JiraLabelUtil.BOLD;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.BoxLayout.Y_AXIS;

public class IssueCreateDialog extends DialogWrapper {
    private final static int DEFAULT_FIELD_WIDTH = 200;
    private final static int DEFAULT_FIELD_HEIGHT = 24;

    private Project project;
    private JPanel issuePanel;
    private JiraIssueForCreate createIssue;

    //FORM
    private JBTextField summaryTextField; //요약
    private JBTextArea descriptionTextArea; //내용

    private CollectionComboBoxModel<JiraProject> projectItems;
    private CollectionComboBoxModel<JiraIssueType> issueTypeItems;
    private CollectionComboBoxModel<JiraIssueUser> assignableUsersItems;

    private ComboBox<JiraProject> projectComboBox;
    private ComboBox<JiraIssueType> issueTypeComboBox;
    private ComboBox<JiraIssueUser> reporterComboBox;
    private ComboBox<JiraIssueUser> assigneeComboBox;

    public IssueCreateDialog(@Nullable Project project, @NotNull JiraIssueForCreate createModel) {
        super(project, false);
        this.project = project;
        this.createIssue = createModel;

        myOKAction = new CreateIssueExecuteAction().disabled();
        init();
    }

    @Override
    protected void init() {
        setTitle("Create Issue for Developers");
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        myOKAction.setEnabled(true);
        issuePanel = new JBPanel(new BorderLayout());
        issuePanel.setLayout(new BoxLayout(issuePanel, Y_AXIS));

        // Project
        JBLabel projectLabel = JiraLabelUtil.createLabel("프로젝트: ").withFont(BOLD);
        projectItems = new CollectionComboBoxModel(new ArrayList());
        for (JiraProject project : createIssue.getProjectList()) {
            JiraProject clone = project.clone();
            projectItems.add(clone);
        }
        projectComboBox = new ComboBox<>(projectItems, DEFAULT_FIELD_WIDTH);
        projectComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
        if (createIssue.getProjectId() != null) {
            projectComboBox.setSelectedItem(new JiraProject(createIssue.getProjectId()));
        } else {
            projectComboBox.setSelectedIndex(0);
        }

        // Summary
        Pattern summaryPattern = Pattern.compile("^(\\[([^]]+)\\])");
        Matcher matcher = summaryPattern.matcher(createIssue.getSummary());
        String subSummary = "";
        String replaceStrDepartment = "[개발]";
        if (nonNull(createIssue.getMyInfo())) {
            if (StringUtils.defaultIfEmpty(createIssue.getMyInfo().getName(), "").contains("Steve") || StringUtils.defaultIfEmpty(createIssue.getMyInfo().getEmailAddress(), "").contains("steve@allwin.bid")) {
                replaceStrDepartment = "[퍼블리싱]";
            }
        }
        if (matcher.find()) {
            subSummary = matcher.replaceFirst(replaceStrDepartment);
        } else {
            subSummary = replaceStrDepartment + createIssue.getSummary();
        }
        JBLabel summaryLabel = JiraLabelUtil.createLabel("요약: ").withFont(BOLD);
        summaryTextField = new JBTextField(subSummary);
        summaryTextField.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));

        // Type
        JBLabel typeLabel = JiraLabelUtil.createLabel("이슈 유형: ").withFont(BOLD);
        createIssueTypeComboBox();

        // Reporter
        JBLabel reporterLabel = JiraLabelUtil.createLabel("보고자: ").withFont(BOLD);
        // Assignee
        JBLabel assigneeLabel = JiraLabelUtil.createLabel("담당자: ").withFont(BOLD);

        createAssignableUserComboBox();


        // Description
        descriptionTextArea = new JBTextArea(createIssue.getDescription(), 5, 60);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);

        issuePanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(projectLabel, projectComboBox)
                .addLabeledComponent(summaryLabel, summaryTextField)
                .addLabeledComponent(typeLabel, issueTypeComboBox)
                .addLabeledComponent(reporterLabel, reporterComboBox)
                .addLabeledComponent(assigneeLabel, assigneeComboBox)
                .addComponent(descriptionTextArea)
                .getPanel();

        projectComboBoxChangeAction();

        return issuePanel;
    }


    private void projectComboBoxChangeAction() {
        this.projectComboBox.addActionListener(e -> {
            getContentPanel().setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
            SwingUtilities.invokeLater(() -> {
                JiraProject selectedItem = (JiraProject) this.projectComboBox.getSelectedItem();
                if (nonNull(selectedItem)) {
                    createIssueTypeComboBox();
                    if (isNull(createIssue.getAssignableUserList())) {
                        createAssignableUserComboBox();
                    }
                }
                getContentPanel().setCursor(Cursor.getDefaultCursor());
            });

        });
    }

    private void createIssueTypeComboBox() {
        JiraServerManager manager = project.getComponent(JiraServerManager.class);
        JiraRestApi jiraRestApi = manager.getJiraRestApi();
        JiraProject selectedProject = (JiraProject) this.projectComboBox.getSelectedItem();
        if (nonNull(selectedProject)) {
            List<JiraIssueType> issueTypeList = ((JiraProject) jiraRestApi.getProject(selectedProject.getKey()).get()).getIssueTypes();
            issueTypeItems = new CollectionComboBoxModel(new ArrayList());
            for (JiraIssueType issueType : issueTypeList) {
                JiraIssueType clone = issueType.clone();
                issueTypeItems.add(clone);
            }

            if (isNull(issueTypeComboBox)) {
                issueTypeComboBox = new ComboBox<>(issueTypeItems);
            } else {
                issueTypeComboBox.setModel(issueTypeItems);
            }
            issueTypeComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
            issueTypeComboBox.setSelectedIndex(0);
            issueTypeComboBox.updateUI();
        }
    }

    private void createAssignableUserComboBox() {
        if (isNull(createIssue.getAssignableUserList())) {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            JiraRestApi jiraRestApi = manager.getJiraRestApi();
            JiraProject selectedProject = (JiraProject) this.projectComboBox.getSelectedItem();
            if (nonNull(selectedProject)) {
                List<JiraIssueUser> assignableUserList = jiraRestApi.getAssignableUsers("project", selectedProject.getKey());
                assignableUsersItems = new CollectionComboBoxModel(new ArrayList());
                for (JiraIssueUser user : assignableUserList) {
                    JiraIssueUser clone = user.clone();
                    assignableUsersItems.add(clone);
                }
            }
        } else {
            assignableUsersItems = new CollectionComboBoxModel(new ArrayList());
            for (JiraIssueUser jiraIssueUser : createIssue.getAssignableUserList()) {
                JiraIssueUser clone = jiraIssueUser.clone();
                assignableUsersItems.add(clone);
            }
        }

        if (isNull(reporterComboBox)) {
            reporterComboBox = new ComboBox<>(assignableUsersItems);
        } else {
            reporterComboBox.setModel(assignableUsersItems);
        }
        reporterComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
        reporterComboBox.setSelectedItem(createIssue.getMyInfo());
        reporterComboBox.updateUI();

        if (isNull(assigneeComboBox)) {
            assigneeComboBox = new ComboBox<>(assignableUsersItems);
        } else {
            assigneeComboBox.setModel(assignableUsersItems);
        }
        assigneeComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
        assigneeComboBox.setSelectedItem(createIssue.getMyInfo());
        assigneeComboBox.updateUI();
    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if (isEmpty(summaryTextField.getText())) {
            return new ValidationInfo("summary body can not be empty!", summaryTextField);
        }
        JiraIssueUser selectedReporter = (JiraIssueUser) reporterComboBox.getSelectedItem();
        if (isNull(selectedReporter)) {
            return new ValidationInfo("reporter can not be empty!", reporterComboBox);
        }
        JiraIssueUser selectedAssignee = (JiraIssueUser) assigneeComboBox.getSelectedItem();
        if (isNull(selectedAssignee)) {
            return new ValidationInfo("assignee can not be empty!", assigneeComboBox);
        }
        JiraIssueType selectedIssueType = (JiraIssueType) issueTypeComboBox.getSelectedItem();
        if (isNull(selectedIssueType)) {
            return new ValidationInfo("issueType can not be empty!", issueTypeComboBox);
        }

        return null;
    }

    @Override
    protected void doOKAction() {
        if (nonNull(project)) {
            JiraIssueForCreate issue = new JiraIssueForCreate();
            JiraProject selectedProject = (JiraProject) projectComboBox.getSelectedItem();
            issue.setProjectId(selectedProject.getId());

            issue.setSummary(summaryTextField.getText());

            JiraIssueType selectedIssueType = (JiraIssueType) issueTypeComboBox.getSelectedItem();
            issue.setIssueTypeId(selectedIssueType.getId());

            JiraIssueUser selectedReporter = (JiraIssueUser) reporterComboBox.getSelectedItem();
            issue.setReporter(selectedReporter.getName());

            JiraIssueUser selectedAssignee = (JiraIssueUser) assigneeComboBox.getSelectedItem();
            issue.setAssignee(selectedAssignee.getName());

            issue.setDescription(descriptionTextArea.getText());
            issue.setParentIssueIdOrKey(createIssue.getParentIssueIdOrKey());

            new CreateIssueTask(project, issue).queue();

        }

        close(0);
    }

    private class CreateIssueExecuteAction extends OkAction {

        public CreateIssueExecuteAction disabled() {
            setEnabled(false);
            return this;
        }
    }


}
