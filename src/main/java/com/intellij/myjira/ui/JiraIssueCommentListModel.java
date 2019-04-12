package com.intellij.myjira.ui;

import com.intellij.myjira.rest.model.JiraIssueComment;
import java.util.List;
import javax.swing.AbstractListModel;

public class JiraIssueCommentListModel extends AbstractListModel<JiraIssueComment> {


    private final List<JiraIssueComment> comments;

    public JiraIssueCommentListModel(List<JiraIssueComment> comments) {
        this.comments = comments;
    }

    @Override
    public int getSize() {
        return comments.size();
    }

    @Override
    public JiraIssueComment getElementAt(int index) {
        return comments.get(index);
    }
}
