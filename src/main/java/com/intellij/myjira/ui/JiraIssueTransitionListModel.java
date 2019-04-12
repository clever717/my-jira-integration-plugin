package com.intellij.myjira.ui;

import com.intellij.myjira.rest.model.JiraIssueTransition;
import java.util.List;
import javax.swing.AbstractListModel;

public class JiraIssueTransitionListModel extends AbstractListModel<JiraIssueTransition> {

    private List<JiraIssueTransition> issueTransitions;

    public JiraIssueTransitionListModel(List<JiraIssueTransition> issueTransitions) {
        this.issueTransitions = issueTransitions;
    }

    @Override
    public int getSize() {
        return issueTransitions.size();
    }

    @Override
    public JiraIssueTransition getElementAt(int index) {
        return issueTransitions.get(index);
    }
}
