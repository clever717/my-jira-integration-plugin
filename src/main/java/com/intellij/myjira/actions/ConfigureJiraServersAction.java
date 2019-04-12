package com.intellij.myjira.actions;

import static java.util.Objects.isNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.ui.dialog.ConfigureJiraServersDialog;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class ConfigureJiraServersAction extends JiraIssueAction {

    private static final ActionProperties properties = ActionProperties.of("Configure Servers...",  AllIcons.General.Settings);

    public ConfigureJiraServersAction() {
        super(properties);
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Project project = e.getProject();
        if(isNull(project)){
            return;
        }

        ConfigureJiraServersDialog dlg = new ConfigureJiraServersDialog(project);
        dlg.show();
    }




}
