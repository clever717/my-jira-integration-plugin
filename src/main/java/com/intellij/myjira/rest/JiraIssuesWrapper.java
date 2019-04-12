package com.intellij.myjira.rest;

import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.util.containers.ContainerUtil;
import java.util.List;

public class JiraIssuesWrapper<T extends JiraIssue> extends JiraResponseWrapper {

    private List<T> issues = ContainerUtil.emptyList();

    public JiraIssuesWrapper() { }

    public List<T> getIssues() {
        return issues;
    }
}
