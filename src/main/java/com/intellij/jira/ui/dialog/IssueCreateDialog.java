package com.intellij.jira.ui.dialog;

import com.intellij.jira.helper.TransitionFieldHelper.FieldEditorInfo;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueForCreate;
import com.intellij.jira.rest.model.JiraIssueType;
import com.intellij.jira.rest.model.JiraIssueUser;
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
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private JiraIssue.Fields fields;
    private String parentIssueIdOrKey;
    private List<JiraIssueUser> assignableUsers;

    private JPanel issuePanel;

    private JiraIssueForCreate createIssue;

    private Map<String, FieldEditorInfo> issueFields = new HashMap<>();

    //FORM
    private JBTextField summaryTextField; //요약
    private JBTextField reporterTextField; //보고자
    private JBTextField assigneeTextField; //담당자
    private JBTextArea descriptionTextArea; //내용

    private ComboBox<JiraIssueType> issueTypeComboBox;
    private CollectionComboBoxModel<JiraIssueType> issueTypeItems;

    private ComboBox<JiraIssueUser> reporterComboBox;
    private ComboBox<JiraIssueUser> assigneeComboBox;
    private CollectionComboBoxModel<JiraIssueUser> assignableUsersItems;

    public IssueCreateDialog(@Nullable Project project, @NotNull JiraIssue.Fields fields, @NotNull List<JiraIssueUser> assignableUsers, @NotNull String parentIssueIdOrKey) {
        super(project, false);
        this.project = project;
        this.fields = fields;
        this.assignableUsers = assignableUsers;
        this.parentIssueIdOrKey = parentIssueIdOrKey;

        myOKAction = new CreateIssueExecuteAction().disabled();
        init();
    }

    @Override
    protected void init() {
        setTitle("Create Sub Issue for Developers");
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        myOKAction.setEnabled(true);
        issuePanel = new JBPanel(new BorderLayout());
        issuePanel.setLayout(new BoxLayout(issuePanel, Y_AXIS));

        // Summary
        Pattern summaryPattern = Pattern.compile("^(\\[([^]]+)\\])");
        Matcher matcher = summaryPattern.matcher(fields.getSummary());
        String subSummary = "";
        String replaceStrDepartment = "[개발]";
        if (fields.getReporter().getName().indexOf("steve") > 0) {
            replaceStrDepartment = "[퍼블리싱]";
        }
        if (matcher.find()) {
            subSummary = matcher.replaceFirst(replaceStrDepartment);
        } else {
            subSummary = replaceStrDepartment + fields.getSummary();
        }
        JBLabel summaryLabel = JiraLabelUtil.createLabel("요약: ").withFont(BOLD);
        summaryTextField = new JBTextField(subSummary);
        summaryTextField.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));

        // Type
        JBLabel typeLabel = JiraLabelUtil.createLabel("이슈 유형: ").withFont(BOLD);

        issueTypeItems = new CollectionComboBoxModel(new ArrayList());
        for (JiraIssueType issueType : fields.getProject().getIssueTypes()) {
            JiraIssueType clone = issueType.clone();
            issueTypeItems.add(clone);
        }
        issueTypeComboBox = new ComboBox<>(issueTypeItems);
        issueTypeComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
        issueTypeComboBox.setSelectedIndex(0);

        // Reporter
        JBLabel reporterLabel = JiraLabelUtil.createLabel("보고자: ").withFont(BOLD);

        assignableUsersItems = new CollectionComboBoxModel(new ArrayList());
        for (JiraIssueUser jiraIssueUser : assignableUsers) {
            JiraIssueUser clone = jiraIssueUser.clone();
            assignableUsersItems.add(clone);
        }
        reporterComboBox = new ComboBox<>(assignableUsersItems);
        reporterComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
        reporterComboBox.setSelectedItem(fields.getReporter());

        // Assignee
        JBLabel assigneeLabel = JiraLabelUtil.createLabel("담당자: ").withFont(BOLD);
        assigneeComboBox = new ComboBox<>(assignableUsersItems);
        assigneeComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
        assigneeComboBox.setSelectedItem(fields.getAssignee());


        // Description
        descriptionTextArea = new JBTextArea(fields.getDescription(), 5, 60);
        descriptionTextArea.setLineWrap(true);
        descriptionTextArea.setWrapStyleWord(true);

        issuePanel = FormBuilder.createFormBuilder()
                .addLabeledComponent(summaryLabel, summaryTextField)
                .addLabeledComponent(typeLabel, issueTypeComboBox)
                .addLabeledComponent(reporterLabel, reporterComboBox)
                .addLabeledComponent(assigneeLabel, assigneeComboBox)
                .addComponent(descriptionTextArea)
                .getPanel();

        return issuePanel;
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
            createIssue = new JiraIssueForCreate();
            createIssue.setSummary(summaryTextField.getText());
            JiraIssueUser selectedReporter = (JiraIssueUser) reporterComboBox.getSelectedItem();
            createIssue.setReporter(selectedReporter.getName());
            JiraIssueUser selectedAssignee = (JiraIssueUser) assigneeComboBox.getSelectedItem();
            createIssue.setAssignee(selectedAssignee.getName());
            createIssue.setDescription(descriptionTextArea.getText());
            createIssue.setProjectId(fields.getProject().getId());
            JiraIssueType selectedIssueType = (JiraIssueType) issueTypeComboBox.getSelectedItem();
            createIssue.setIssueTypeId(selectedIssueType.getId()); //작업 - 해야할일
            createIssue.setParentIssueIdOrKey(parentIssueIdOrKey);

            new CreateIssueTask(project, createIssue).queue();
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
