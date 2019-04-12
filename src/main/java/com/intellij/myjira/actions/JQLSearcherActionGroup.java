package com.intellij.myjira.actions;

import static java.util.Objects.isNull;

import com.intellij.icons.AllIcons;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.Separator;
import com.intellij.openapi.project.Project;

public class JQLSearcherActionGroup extends DefaultActionGroup {

    public JQLSearcherActionGroup() {
        super("JQL Searchers", true);
        getTemplatePresentation().setIcon(AllIcons.Vcs.Changelist);
        add(new AddJQLSearcherAction());
        add(new EditJQLSearcherAction());
        add(new Separator());
        add(new ConfigureJQLSearchersAction());
    }

    @Override
    public void update(AnActionEvent event) {
        Project project = event.getProject();
        if (isNull(project)|| !project.isInitialized() || project.isDisposed()) {
            event.getPresentation().setVisible(false);
        } else {
            JiraServerManager manager = project.getComponent(JiraServerManager.class);
            if(manager.hasJiraServerConfigured()){
                event.getPresentation().setVisible(true);
            }
            else{
                event.getPresentation().setVisible(false);
            }
        }
    }

}
