package com.intellij.jira.ui.dialog;

import com.intellij.jira.helper.TransitionFieldHelper;
import com.intellij.jira.helper.TransitionFieldHelper.FieldEditorInfo;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueFieldProperties;
import com.intellij.jira.rest.model.JiraIssueTransition;
import com.intellij.jira.tasks.TransitIssueTask;
import com.intellij.jira.ui.JiraIssueTransitionListModel;
import com.intellij.jira.ui.renders.JiraIssueTransitionListCellRenderer;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.tasks.jira.JiraRepository;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.JBDimension;
import com.intellij.util.ui.JBUI;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.intellij.jira.helper.TransitionFieldHelper.createCommentFieldEditorInfo;
import static com.intellij.jira.ui.editors.FieldEditorFactory.createCommentFieldEditor;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class IssueTransitionDialog extends DialogWrapper {

    private Project project;
    private JiraIssue issue;

    private List<JiraIssueTransition> transitions;
    private JiraIssueTransition selectedIssueTransition;

    private JPanel transitionsPanel;
    private JPanel transitionFieldsPanel;


    private Map<String, FieldEditorInfo> requiredFields = new HashMap<>();
    private Map<String, FieldEditorInfo> optionalFields = new HashMap<>();


    public IssueTransitionDialog(@Nullable Project project, @NotNull JiraIssue issue, List<JiraIssueTransition> transitions) {
        super(project, false);
        this.project = project;
        this.issue = issue;
        this.transitions = transitions;
        myOKAction = new TransitIssueExecuteAction().disabled();
        init();
    }

    @Override
    protected void init() {
        setTitle("Transit Issue " + issue.getKey());
        super.init();
    }

    @Nullable
    @Override
    protected JComponent createCenterPanel() {
        JBPanel panel = new JBPanel(new BorderLayout());
        transitionsPanel = new JBPanel(new BorderLayout());
        JBList<JiraIssueTransition> transitionList = new JBList<>();
        transitionList.setEmptyText("No transitions");
        transitionList.setModel(new JiraIssueTransitionListModel(transitions));
        transitionList.setCellRenderer(new JiraIssueTransitionListCellRenderer());
        transitionList.setSelectionMode(SINGLE_SELECTION);
        transitionList.setPreferredSize(new JBDimension(100, 300));
        transitionList.setBorder(BorderFactory.createLineBorder(JBColor.border()));
        transitionList.addListSelectionListener(e -> {
                SwingUtilities.invokeLater(() -> updateTransitionFieldPanel(transitionList.getSelectedValue()));
        });

        transitionsPanel.add(transitionList, BorderLayout.CENTER);
        transitionFieldsPanel = new JBPanel(new GridBagLayout());
        transitionFieldsPanel.setBorder(JBUI.Borders.empty(5));
        transitionFieldsPanel.add(JiraPanelUtil.createPlaceHolderPanel("Select transition"), new GridBagConstraints());

        panel.add(transitionsPanel, BorderLayout.WEST);
        panel.add(ScrollPaneFactory.createScrollPane(transitionFieldsPanel, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER), BorderLayout.CENTER);
        panel.setPreferredSize(new JBDimension(500, 300));

        return panel;
    }

    private void updateTransitionFieldPanel(JiraIssueTransition transition) {
        selectedIssueTransition = transition;
        myOKAction.setEnabled(true);

        List<JiraIssueFieldProperties> transitionFields = transition.getFields().entrySet().stream()
                .map(entry -> JiraRepository.GSON.fromJson(entry.getValue(), JiraIssueFieldProperties.class))
                .collect(Collectors.toList());


        requiredFields.clear();
        optionalFields.clear();

        transitionFieldsPanel.removeAll();
        transitionFieldsPanel.setLayout(new GridBagLayout());

        if(transitionFields.isEmpty()){
            transitionFieldsPanel.add(JiraPanelUtil.createPlaceHolderPanel("No fields required"), new GridBagConstraints());
        }else{
            createTransitionFields(transitionFields);
        }


        transitionFieldsPanel.revalidate();
        transitionFieldsPanel.repaint();

    }

    private void createTransitionFields(List<JiraIssueFieldProperties> transitionFields) {

        if(!transitionFields.isEmpty()){
            FormBuilder formBuilder = FormBuilder.createFormBuilder().setAlignLabelOnRight(true);

            transitionFields.forEach(fieldProperties -> {

                FieldEditorInfo info = TransitionFieldHelper.createFieldEditorInfo(fieldProperties, issue);
                if(info.isRequired()){
                    requiredFields.put(info.getName(), info);
                }else{
                    optionalFields.put(info.getName(), info);
                }

                formBuilder.addComponent(info.getPanel());

            });

            FieldEditorInfo commentInfo = createCommentFieldEditorInfo(createCommentFieldEditor(issue.getKey()));
            optionalFields.put(commentInfo.getName(), commentInfo);
            formBuilder.addComponent(commentInfo.getPanel());

            GridBagConstraints constraints = new GridBagConstraints();
            constraints.anchor = GridBagConstraints.NORTH;
            constraints.gridx = 0;
            constraints.gridy = 0;
            constraints.weighty = 1;



            transitionFieldsPanel.add(formBuilder.getPanel(), constraints);

        }

    }

    @Nullable
    @Override
    protected ValidationInfo doValidate() {
        if(isNull(selectedIssueTransition)){
            return new ValidationInfo("You must select transition");
        }

        for(FieldEditorInfo info : requiredFields.values()){
            for(Map.Entry<String, String> entry : info.getInputValues().entrySet()){
                if(isEmpty(entry.getValue())){
                    return new ValidationInfo(String.format("%s is required", entry.getKey()));
                }
            }

        }


        return null;
    }


    @Override
    protected void doOKAction() {
        if(nonNull(project)){
            new TransitIssueTask(project, issue.getId(), selectedIssueTransition.getId(), requiredFields, optionalFields).queue();
        }

        close(0);
    }

    private class TransitIssueExecuteAction extends OkAction{

        public TransitIssueExecuteAction disabled(){
            setEnabled(false);
            return this;
        }
    }


}