package com.intellij.myjira.rest;

import com.intellij.myjira.rest.model.JiraIssueLinkType;
import com.intellij.util.containers.ContainerUtil;
import java.util.List;

public class JiraIssueLinkTypesWrapper {

    private List<JiraIssueLinkType> issueLinkTypes = ContainerUtil.emptyList();

    public JiraIssueLinkTypesWrapper() { }

    public List<JiraIssueLinkType> getIssueLinkTypes() {
        return issueLinkTypes;
    }
}
