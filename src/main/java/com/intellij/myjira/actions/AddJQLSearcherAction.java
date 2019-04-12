package com.intellij.myjira.actions;

import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.dialog.NewJQLSearcherDialog;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;

public class AddJQLSearcherAction extends AnAction {

    public AddJQLSearcherAction() {
        super("New JQL searcher", null, AllIcons.General.Add);
    }

    @Override
    public void actionPerformed(AnActionEvent e) {
        Project project = e.getProject();
        if(nonNull(project)){
            NewJQLSearcherDialog dialog = new NewJQLSearcherDialog(project);
            dialog.show();
        }

    }

    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        if (isNull(project)|| !project.isInitialized() || project.isDisposed()) {
            event.getPresentation().setEnabled(false);
        } else {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            if(manager.hasJiraServerConfigured()){
                 event.getPresentation().setEnabled(true);
            }
            else{
                event.getPresentation().setEnabled(false);
            }
        }
    }


}
