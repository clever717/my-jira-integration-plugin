package com.intellij.myjira.ui;

import com.intellij.myjira.rest.model.JiraIssueLink;
import java.util.List;
import javax.swing.AbstractListModel;

public class JiraIssueLinkListModel extends AbstractListModel<JiraIssueLink> {

    private List<JiraIssueLink> issueLinks;

    public JiraIssueLinkListModel(List<JiraIssueLink> issueLinks) {
        this.issueLinks = issueLinks;
    }

    @Override
    public int getSize() {
        return issueLinks.size();
    }



    @Override
    public JiraIssueLink getElementAt(int index) {
        return issueLinks.get(index);
    }
}
