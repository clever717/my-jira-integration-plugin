package com.intellij.myjira.rest;

import com.intellij.myjira.rest.model.JiraIssueTransition;
import com.intellij.util.containers.ContainerUtil;
import java.util.List;

public class JiraIssueTransitionsWrapper <T extends JiraIssueTransition> extends JiraResponseWrapper {

    private List<T> transitions = ContainerUtil.emptyList();

    public JiraIssueTransitionsWrapper() { }

    public List<T> getTransitions() {
        return transitions;
    }
}
