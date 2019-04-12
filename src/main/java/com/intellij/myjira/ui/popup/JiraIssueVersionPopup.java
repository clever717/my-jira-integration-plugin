package com.intellij.myjira.ui.popup;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Conditions;
import com.intellij.ui.popup.PopupFactoryImpl;
import org.jetbrains.annotations.NotNull;

public class JiraIssueVersionPopup extends PopupFactoryImpl.ActionGroupPopup {

    public JiraIssueVersionPopup(@NotNull ActionGroup actionGroup, @NotNull Project project) {
        super("Change Version to", actionGroup, SimpleDataContext.getProjectContext(project), false, false, false, false, null, -1, Conditions.alwaysTrue(), null);
    }


}
