package com.intellij.myjira.rest;

import com.intellij.myjira.rest.model.JiraIssueComment;
import com.intellij.util.containers.ContainerUtil;
import java.util.List;

public class JiraIssueCommentsWrapper extends JiraResponseWrapper {

    private List<JiraIssueComment> comments = ContainerUtil.emptyList();

    protected JiraIssueCommentsWrapper() { }

    public List<JiraIssueComment> getComments() {
        return comments;
    }
}
