package com.intellij.jira.ui.panels;

import com.intellij.jira.components.JiraActionManager;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.util.JiraIconUtil;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.tools.SimpleActionGroup;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import org.fest.util.Lists;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

public class JiraIssueDetailsPanel extends SimpleToolWindowPanel {

    private ActionToolbar toolbar;
    private JBPanel mainPanel;
    private JiraIssue currentIssue;

    public JiraIssueDetailsPanel(JiraIssue issue) {
        super(true, true);
        setToolbar();
        setMainPanel(issue);
    }

    public void updateIssue(JiraIssue issue){
        if(!currentIssue.getKey().equals(issue.getKey())) {
            this.mainPanel.removeAll();
            setMainPanel(issue);
            this.mainPanel.repaint();
        }
    }

    private void setMainPanel(JiraIssue issue) {
        this.currentIssue = issue;
        this.mainPanel = new JBPanel(new BorderLayout())
                            .withBackground(JBColor.WHITE)
                            .withBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));


        JBPanel issueDetails = new JBPanel().withBackground(JBColor.WHITE);
        issueDetails.setLayout(new BoxLayout(issueDetails, BoxLayout.Y_AXIS));

        // Key
        JBPanel issueKeyPanel = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(JiraPanelUtil.MARGIN_BOTTOM);
        JBLabel keyLabel = JiraLabelUtil.createLabel(issue.getKey());
        issueKeyPanel.add(keyLabel, BorderLayout.LINE_START);

        // Summary
        JBPanel issueSummaryPanel = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(JiraPanelUtil.MARGIN_BOTTOM);
        JBLabel summaryLabel = JiraLabelUtil.createLabel(issue.getSummary());
        issueSummaryPanel.add(summaryLabel, BorderLayout.LINE_START);

        // Type and Status
        JBPanel typeAndStatusPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(JiraPanelUtil.MARGIN_BOTTOM);
        JBPanel typePanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel typeLabel = JiraLabelUtil.createLabel("Type: ").withFont(JiraLabelUtil.BOLD);
        JBLabel typeValueLabel = JiraLabelUtil.createIconLabel(issue.getIssuetype().getIconUrl(), issue.getIssuetype().getName());

        typePanel.add(typeLabel, BorderLayout.LINE_START);
        typePanel.add(typeValueLabel, BorderLayout.CENTER);

        JBPanel statusPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel statusLabel = JiraLabelUtil.createLabel("Status: ").withFont(JiraLabelUtil.BOLD);
        JBLabel statusValueLabel = JiraLabelUtil.createIconLabel(issue.getStatus().getIconUrl(), issue.getStatus().getName());

        statusPanel.add(statusLabel, BorderLayout.LINE_START);
        statusPanel.add(statusValueLabel, BorderLayout.CENTER);

        typeAndStatusPanel.add(typePanel);
        typeAndStatusPanel.add(statusPanel);

        // Priority and Assignee
        JBPanel priorityAndAssigneePanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(JiraPanelUtil.MARGIN_BOTTOM);
        JBPanel priorityPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel priorityLabel = JiraLabelUtil.createLabel("Priority: ").withFont(JiraLabelUtil.BOLD);
        JBLabel priorityValueLabel = JiraLabelUtil.createIconLabel(JiraIconUtil.getSmallIcon(issue.getPriority().getIconUrl()), issue.getPriority().getName());

        priorityPanel.add(priorityLabel, BorderLayout.LINE_START);
        priorityPanel.add(priorityValueLabel, BorderLayout.CENTER);

        JBPanel assigneePanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel assigneeLabel = JiraLabelUtil.createLabel("Assigne: ").withFont(JiraLabelUtil.BOLD);
        JBLabel assigneeValueLabel = JiraLabelUtil.createLabel(issue.getAssignee() != null ? issue.getAssignee().getDisplayName() : "-");

        assigneePanel.add(assigneeLabel, BorderLayout.LINE_START);
        assigneePanel.add(assigneeValueLabel, BorderLayout.CENTER);

        priorityAndAssigneePanel.add(priorityPanel);
        priorityAndAssigneePanel.add(assigneePanel);

        // Description
        JBPanel issueDescriptionPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel descriptionLabel = JiraLabelUtil.createLabel("Description").withFont(JiraLabelUtil.BOLD);
        JTextArea descriptionArea = new JTextArea(issue.getDescription());
        descriptionArea.setLineWrap(true);

        issueDescriptionPanel.add(descriptionLabel, BorderLayout.PAGE_START);
        issueDescriptionPanel.add(descriptionArea, BorderLayout.CENTER);

        issueDetails.add(issueKeyPanel);
        issueDetails.add(issueSummaryPanel);
        issueDetails.add(typeAndStatusPanel);
        issueDetails.add(priorityAndAssigneePanel);
        issueDetails.add(issueDescriptionPanel);

        this.mainPanel.add(ScrollPaneFactory.createScrollPane(issueDetails, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), BorderLayout.CENTER);
        super.setContent(this.mainPanel);

    }

    private void setToolbar(){
        this.toolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
        this.toolbar.setTargetComponent(this);

        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(this.toolbar.getComponent());
        super.setToolbar(toolBarBox);
    }



    private ActionGroup createActionGroup(){
        SimpleActionGroup group = new SimpleActionGroup();
        getIssuePanelActions().forEach((group)::add);
        return group;
    }

    private List<AnAction> getIssuePanelActions(){
        return Lists.newArrayList(JiraActionManager.getInstance().getJiraIssuesRefreshAction());
    }
}
