package com.intellij.jira.ui.popup;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Conditions;
import com.intellij.ui.popup.PopupFactoryImpl;
import org.jetbrains.annotations.NotNull;

public class JiraIssueTypePopup extends PopupFactoryImpl.ActionGroupPopup {

    public JiraIssueTypePopup(@NotNull ActionGroup actionGroup, @NotNull Project project) {
        super("Choose Issue Type", actionGroup, SimpleDataContext.getProjectContext(project), false, false, false, false, null, -1, Conditions.alwaysTrue(), null);
    }


}
