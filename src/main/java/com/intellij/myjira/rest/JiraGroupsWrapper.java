package com.intellij.myjira.rest;

import com.intellij.myjira.rest.model.JiraGroup;
import com.intellij.util.containers.ContainerUtil;
import java.util.List;

public class JiraGroupsWrapper {

    private List<JiraGroup> groups = ContainerUtil.emptyList();

    public JiraGroupsWrapper() { }

    public List<JiraGroup> getGroups() {
        return groups;
    }
}
