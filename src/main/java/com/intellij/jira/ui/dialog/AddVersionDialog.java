package com.intellij.jira.ui.dialog;

import com.intellij.jira.rest.model.JiraProject;
import com.intellij.jira.tasks.AddVersionTask;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.CollectionComboBoxModel;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.BoxLayout.Y_AXIS;

public class AddVersionDialog extends DialogWrapper {
    private final static int DEFAULT_FIELD_WIDTH = 200;
    private final static int DEFAULT_FIELD_HEIGHT = 24;

    private Project project;
    private List<JiraProject> projectList;
    private CollectionComboBoxModel<JiraProject> projectItems;
    private ComboBox<JiraProject> projectComboBox;

    private JBTextField nameTextField;
    private JBTextField descriptionTextField;

    public AddVersionDialog(@Nullable Project project, List<JiraProject> projectList) {
        super(project, false);
        this.project = project;
        this.projectList = projectList;
        init();
    }


    @Override
    protected void init() {
        setTitle("Add a version");
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JPanel panel = new JBPanel(new BorderLayout());
        panel.setLayout(new BoxLayout(panel, Y_AXIS));

        JBLabel projectLabel = JiraLabelUtil.createBoldLabel("프로젝트 : ");
        projectItems = new CollectionComboBoxModel(new ArrayList());
        for (JiraProject project : projectList) {
            JiraProject clone = project.clone();
            projectItems.add(clone);
        }
        projectComboBox = new ComboBox<>(projectItems, DEFAULT_FIELD_WIDTH);
        projectComboBox.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));
        projectComboBox.setSelectedIndex(0);

        JBLabel nameLabel = JiraLabelUtil.createBoldLabel("이름 : ");
        nameTextField = new JBTextField();
        nameTextField.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));

        JBLabel descriptionLabel = JiraLabelUtil.createBoldLabel("내용 : ");
        descriptionTextField = new JBTextField();
        descriptionTextField.setPreferredSize(UI.size(DEFAULT_FIELD_WIDTH, DEFAULT_FIELD_HEIGHT));

        panel = FormBuilder.createFormBuilder()
                .addLabeledComponent(projectLabel, projectComboBox)
                .addLabeledComponent(nameLabel, nameTextField)
                .addLabeledComponent(descriptionLabel, descriptionTextField)
                .getPanel();
        return panel;
    }


    @NotNull
    @Override
    protected Action[] createActions() {
        return new Action[]{new AddVersionExecuteAction(), myCancelAction};
    }


    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        JiraProject selectedProject = (JiraProject) projectComboBox.getSelectedItem();
        if (isNull(selectedProject)) {
            return new ValidationInfo("project can not be empty!", projectComboBox);
        }
        if (isEmpty(nameTextField.getText())) {
            return new ValidationInfo("name can not be empty!", nameTextField);
        }
        if (isEmpty(descriptionTextField.getText())) {
            return new ValidationInfo("description  can not be empty!", descriptionTextField);
        }
        return null;
    }

    @Override
    protected void doOKAction() {
        if (nonNull(project)) {
            JiraProject selectedProject = (JiraProject) projectComboBox.getSelectedItem();
            new AddVersionTask(project, selectedProject.getKey(), selectedProject.getId(), nameTextField.getText(), descriptionTextField.getText()).queue();
        }
        close(0);
    }

    private class AddVersionExecuteAction extends OkAction {

    }

}
