package com.intellij.jira.ui.panels;

import static com.intellij.jira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static com.intellij.jira.util.JiraLabelUtil.BOLD;
import static com.intellij.jira.util.JiraLabelUtil.ITALIC;
import static com.intellij.jira.util.JiraLabelUtil.createEmptyLabel;
import static com.intellij.jira.util.JiraLabelUtil.createIconLabel;
import static com.intellij.jira.util.JiraPanelUtil.MARGIN_BOTTOM;
import static java.awt.BorderLayout.CENTER;
import static java.awt.BorderLayout.LINE_START;
import static java.awt.BorderLayout.PAGE_START;
import static java.util.Objects.nonNull;
import static javax.swing.BoxLayout.X_AXIS;
import static javax.swing.BoxLayout.Y_AXIS;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

import com.intellij.jira.actions.AddJiraSubIssueDialogAction;
import com.intellij.jira.actions.JiraIssueActionGroup;
import com.intellij.jira.actions.JiraIssueAssigneePopupAction;
import com.intellij.jira.actions.JiraIssuePrioritiesPopupAction;
import com.intellij.jira.actions.JiraIssueTransitionDialogAction;
import com.intellij.jira.actions.JiraIssueVersionPopupAction;
import com.intellij.jira.rest.model.JiraIssue;
import com.intellij.jira.rest.model.JiraIssueAttachment;
import com.intellij.jira.rest.model.JiraProjectVersionDetails;
import com.intellij.jira.util.JiraIconUtil;
import com.intellij.jira.util.JiraIssueUtil;
import com.intellij.jira.util.JiraLabelUtil;
import com.intellij.jira.util.JiraPanelUtil;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.JBColor;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.util.List;
import java.util.stream.Collectors;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import org.jetbrains.annotations.NotNull;

class JiraIssuePreviewPanel extends SimpleToolWindowPanel {

    private JiraIssue issue;

    JiraIssuePreviewPanel(@NotNull JiraIssue issue) {
        super(true, true);
        this.issue = issue;
        setBackground(JBColor.white);
        initToolbar();
        initContent();
    }

    private void initToolbar(){
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
        actionToolbar.setTargetComponent(this);

        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(actionToolbar.getComponent());
        setToolbar(toolBarBox);
    }

    private void initContent() {
        JBPanel previewPanel = new JBPanel(new BorderLayout())
                .withBackground(JBColor.WHITE)
                .withBorder(JBUI.Borders.empty(1, 5, 1, 0));

        JBPanel issueDetails = new JBPanel().withBackground(JBColor.WHITE);
        issueDetails.setLayout(new BoxLayout(issueDetails, Y_AXIS));

        // Key and updated
        JBPanel keyAndUpdatedPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
        JBPanel issueAndProjectKeyPanel = new JBPanel().withBackground(JBColor.WHITE);
        issueAndProjectKeyPanel.setLayout(new BoxLayout(issueAndProjectKeyPanel, X_AXIS));
        JBLabel projectKeyLabel = JiraLabelUtil.createLinkLabel(issue.getProject().getName(), issue.getProject().getUrl());
        JBLabel separatorLabel = JiraLabelUtil.createLabel(" / ");
        JBLabel issueKeyLabel = JiraLabelUtil.createLinkLabel(issue.getKey(), issue.getUrl());

        issueAndProjectKeyPanel.add(projectKeyLabel);
        issueAndProjectKeyPanel.add(separatorLabel);
        issueAndProjectKeyPanel.add(issueKeyLabel);

        JBLabel updatedLabel = JiraLabelUtil.createLabel(JiraIssueUtil.getUpdated(issue), SwingConstants.RIGHT).withFont(ITALIC);
        updatedLabel.setForeground(JBColor.darkGray);
        updatedLabel.setToolTipText("Updated");

        keyAndUpdatedPanel.add(issueAndProjectKeyPanel);
        keyAndUpdatedPanel.add(updatedLabel);

        // Summary
        JBPanel issueSummaryPanel = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(MARGIN_BOTTOM);
        JBLabel summaryLabel = JiraLabelUtil.createLabel(issue.getSummary());
        issueSummaryPanel.add(summaryLabel, LINE_START);

        // Type and Status
        JBPanel typeAndStatusPanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
        JBPanel typePanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel typeLabel = JiraLabelUtil.createLabel("Type: ").withFont(BOLD);
        JBLabel typeValueLabel = JiraLabelUtil.createLabel(issue.getIssuetype().getName());

        typePanel.add(typeLabel, LINE_START);
        typePanel.add(typeValueLabel, CENTER);

        JBPanel statusPanel = new JBPanel().withBackground(JBColor.white);
        statusPanel.setLayout(new BoxLayout( statusPanel, X_AXIS));

        JBLabel statusLabel = JiraLabelUtil.createLabel("Status: ").withFont(BOLD);
        JLabel statusValueLabel = JiraLabelUtil.createStatusLabel(issue.getStatus());

        statusPanel.add(statusLabel);
        statusPanel.add(statusValueLabel);

        typeAndStatusPanel.add(typePanel);
        typeAndStatusPanel.add(statusPanel);

        // Priority and Assignee
        JBPanel priorityAndAssigneePanel = JiraPanelUtil.createWhitePanel(new GridLayout(1, 2)).withBorder(MARGIN_BOTTOM);
        JBPanel priorityPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel priorityLabel = JiraLabelUtil.createLabel("Priority: ").withFont(BOLD);
        JBLabel priorityValueLabel = nonNull(issue.getPriority()) ? createIconLabel(JiraIconUtil.getSmallIcon(issue.getPriority().getIconUrl()), issue.getPriority().getName()) : createEmptyLabel();

        priorityPanel.add(priorityLabel, LINE_START);
        priorityPanel.add(priorityValueLabel, CENTER);

        JBPanel assigneePanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel assigneeLabel = JiraLabelUtil.createLabel("Assigne: ").withFont(BOLD);
        JBLabel assigneeValueLabel = JiraLabelUtil.createLabel(issue.getAssignee() != null ? issue.getAssignee().getDisplayName() : "");

        assigneePanel.add(assigneeLabel, LINE_START);
        assigneePanel.add(assigneeValueLabel, CENTER);

        priorityAndAssigneePanel.add(priorityPanel);
        priorityAndAssigneePanel.add(assigneePanel);

        // Versions
        JBPanel versionsPanel = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(MARGIN_BOTTOM);
        JBLabel versionsLabel = JiraLabelUtil.createLabel("Versions: ").withFont(BOLD);
        JBLabel versionsValueLabel = JiraLabelUtil.createLabel(getVersionsNames(issue.getFixVersions()));

        versionsPanel.add(versionsLabel, LINE_START);
        versionsPanel.add(versionsValueLabel, CENTER);

        // Attachment
        JBPanel issueAttachmentPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel attachmentLabel = JiraLabelUtil.createLabel("Attachment: ").withFont(BOLD).withBorder(MARGIN_BOTTOM);

        JBPanel attachmentArea = JiraPanelUtil.createWhitePanel(new BorderLayout()).withBorder(MARGIN_BOTTOM);
        for (JiraIssueAttachment attachment : issue.getAttatchment()) {
            JBLabel fileLink = JiraLabelUtil.createLinkLabel(attachment.getFilename(), attachment.getContent());
            attachmentArea.add(fileLink);
        }

        issueAttachmentPanel.add(attachmentLabel, PAGE_START);
        issueAttachmentPanel.add(attachmentArea, CENTER);

        // Description
        JBPanel issueDescriptionPanel = JiraPanelUtil.createWhitePanel(new BorderLayout());
        JBLabel descriptionLabel = JiraLabelUtil.createLabel("Description: ").withFont(BOLD).withBorder(MARGIN_BOTTOM);
        JTextArea descriptionArea = new JTextArea(nonNull(issue.getDescription()) ? issue.getDescription() : "");
        descriptionArea.setLineWrap(true);
        descriptionArea.setEditable(false);

        issueDescriptionPanel.add(descriptionLabel, PAGE_START);
        issueDescriptionPanel.add(descriptionArea, CENTER);

        issueDetails.add(keyAndUpdatedPanel);
        issueDetails.add(issueSummaryPanel);
        issueDetails.add(typeAndStatusPanel);
        issueDetails.add(priorityAndAssigneePanel);
        issueDetails.add(versionsPanel);
        issueDetails.add(issueAttachmentPanel);
        issueDetails.add(issueDescriptionPanel);

        previewPanel.add(ScrollPaneFactory.createScrollPane(issueDetails, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_AS_NEEDED), CENTER);


        setContent(previewPanel);
    }


    private ActionGroup createActionGroup(){
        JiraIssueActionGroup group = new JiraIssueActionGroup(this);
        group.add(new JiraIssueTransitionDialogAction(() -> issue));
        group.add(new JiraIssueAssigneePopupAction(() -> issue));
        group.add(new JiraIssuePrioritiesPopupAction(() -> issue));
        group.add(new JiraIssueVersionPopupAction(() -> issue));
        group.add(new AddJiraSubIssueDialogAction(() -> issue));

        return group;
    }

    private String getVersionsNames(List<JiraProjectVersionDetails> versions) {
        if(versions.isEmpty()){
            return "None";
        }

        return versions.stream()
                .map(JiraProjectVersionDetails::getName)
                .collect(Collectors.joining(", "));
    }

    private void openWebPage(String url) {
        try {
            java.awt.Desktop.getDesktop().browse(java.net.URI.create(url));
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
    }
}
