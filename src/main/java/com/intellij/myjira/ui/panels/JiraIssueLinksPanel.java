package com.intellij.myjira.ui.panels;

import static java.awt.BorderLayout.CENTER;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

import com.intellij.myjira.rest.model.JiraIssueLink;
import com.intellij.myjira.ui.JiraIssueLinkListModel;
import com.intellij.myjira.ui.renders.JiraIssueLinkListCellRenderer;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import java.awt.BorderLayout;
import java.util.List;

class JiraIssueLinksPanel extends SimpleToolWindowPanel {

    private List<JiraIssueLink> issueLinks;

    JiraIssueLinksPanel(List<JiraIssueLink> issueLinks) {
        super(true, true);
        this.issueLinks = issueLinks;
        initContent();
    }

    private void initContent() {
        JBPanel panel = new JBPanel(new BorderLayout());

        JBList<JiraIssueLink> issueLinkList = new JBList<>();
        issueLinkList.setEmptyText("No links");
        issueLinkList.setModel(new JiraIssueLinkListModel(issueLinks));
        issueLinkList.setCellRenderer(new JiraIssueLinkListCellRenderer());

        panel.add(ScrollPaneFactory.createScrollPane(issueLinkList, VERTICAL_SCROLLBAR_AS_NEEDED, HORIZONTAL_SCROLLBAR_NEVER), CENTER);

        setContent(panel);
    }


}
