package com.intellij.myjira.ui;

import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.server.JiraRestApi;
import com.intellij.myjira.server.JiraServerManager;
import com.intellij.myjira.ui.panels.JiraIssuesPanel;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowFactory;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import com.intellij.ui.content.ContentManager;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

public class JiraToolWindowFactory implements ToolWindowFactory {

    public static final String TOOL_WINDOW_ID = "JIRA";
    public static final String TAB_ISSUES = "Issues";

    private JiraIssuesPanel issuesPanel;

    @Override
    public void createToolWindowContent(@NotNull Project project, @NotNull ToolWindow toolWindow) {
        createContent(project, toolWindow);

        project.getComponent(JiraServerManager.class).addConfigurationServerChangedListener(() -> {
            SwingUtilities.invokeLater(() -> createContent(project, toolWindow));
        });

        toolWindow.setType(ToolWindowType.DOCKED, null);
    }

    private void createContent(Project project, ToolWindow toolWindow) {
        ContentManager contentManager = toolWindow.getContentManager();
        contentManager.removeAllContents(true);

        JiraRestApi jiraRestApi = project.getComponent(JiraServerManager.class).getJiraRestApi();
        issuesPanel = new JiraIssuesPanel(jiraRestApi, project);

        Content content = contentManager.getFactory().createContent(issuesPanel, TAB_ISSUES, false);
        contentManager.addDataProvider(issuesPanel);
        contentManager.addContent(content);
    }


    public void update(JiraIssue issue){
        issuesPanel.update(issue);
    }

}
