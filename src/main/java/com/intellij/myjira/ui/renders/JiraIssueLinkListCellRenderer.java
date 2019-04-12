package com.intellij.myjira.ui.renders;

import static com.intellij.myjira.util.JiraLabelUtil.BOLD;
import static com.intellij.myjira.util.JiraLabelUtil.DARCULA_ISSUE_LINK_COLOR;
import static com.intellij.myjira.util.JiraLabelUtil.IN_PROGRESS_TEXT_COLOR;
import static com.intellij.myjira.util.JiraLabelUtil.ISSUE_LINK_COLOR;
import static com.intellij.myjira.util.JiraLabelUtil.getFgRowColor;
import static com.intellij.openapi.util.text.StringUtil.toUpperCase;
import static java.util.Objects.nonNull;

import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssueLink;
import com.intellij.myjira.rest.model.JiraIssueStatus;
import com.intellij.myjira.util.JiraLabelUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import com.intellij.ui.components.JBPanel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import javax.swing.JList;

public class JiraIssueLinkListCellRenderer extends DefaultJiraListCellRender {

    private JBLabel typeLabel;
    private JBLabel issueKeyAndSummaryLabel;
    private JBLabel issueStatusLabel;

    public JiraIssueLinkListCellRenderer() {
        super();
        init();
    }

    private void init() {
        JBPanel issueLinkpanel = new JBPanel(new BorderLayout())
                .withBorder(JBUI.Borders.empty(4, 5)).andTransparent();
        typeLabel =  JiraLabelUtil.createEmptyLabel().withFont(BOLD);
        issueStatusLabel = JiraLabelUtil.createEmptyStatusLabel();
        issueKeyAndSummaryLabel = JiraLabelUtil.createEmptyLabel().withBorder(JBUI.Borders.emptyLeft(10));
        issueLinkpanel.add(typeLabel, BorderLayout.LINE_START);
        issueLinkpanel.add(issueKeyAndSummaryLabel, BorderLayout.CENTER);
        issueLinkpanel.add(issueStatusLabel, BorderLayout.LINE_END);
        add(issueLinkpanel);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        JiraIssueLink issueLink = (JiraIssueLink) value;

        setBorder(JBUI.Borders.emptyBottom(2));
        setBackground(UIUtil.isUnderDarcula() ? DARCULA_ISSUE_LINK_COLOR : ISSUE_LINK_COLOR);

        String typeText = nonNull(issueLink.getInwardIssue()) ? issueLink.getType().getInward() : issueLink.getType().getOutward();
        typeLabel.setText(typeText);
        typeLabel.setForeground(getFgRowColor());

        JiraIssue issue = nonNull(issueLink.getInwardIssue()) ? issueLink.getInwardIssue() : issueLink.getOutwardIssue();
        JiraIssueStatus status = issue.getStatus();

        String issueKeyAndSummaryText = getIssueKeyAndSummary(issue);
        issueKeyAndSummaryLabel.setText(issueKeyAndSummaryText);
        issueKeyAndSummaryLabel.setForeground(issue.isResolved() ? JBColor.border() : list.getForeground());


        issueStatusLabel.setText(toUpperCase(status.getName()));
        issueStatusLabel.setBackground(status.getCategoryColor());
        issueStatusLabel.setForeground(status.isInProgressCategory() ?  IN_PROGRESS_TEXT_COLOR : Color.WHITE);


        return this;
    }


    private String getIssueKeyAndSummary(JiraIssue issue){
        return issue.getKey() + " " + issue.getSummary();
    }


}
