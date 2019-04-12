package com.intellij.myjira.components;

import com.intellij.myjira.events.JiraIssueEventListener;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.openapi.components.ProjectComponent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.SwingUtilities;

public class JiraIssueUpdater implements ProjectComponent, Updater<JiraIssue>  {

    private List<JiraIssueEventListener> listeners;


    @Override
    public void projectOpened() {
        listeners = new ArrayList<>();
    }

    @Override
    public void projectClosed() {
        listeners.clear();
    }



    public void addListener(JiraIssueEventListener listener){
        listeners.add(listener);
    }

    @Override
    public void update(List<JiraIssue> issues) {
        listeners.forEach(j ->
                SwingUtilities.invokeLater(() -> j.update(issues)));
    }

    @Override
    public void update(JiraIssue issue) {
        listeners.forEach(j ->
            SwingUtilities.invokeLater(() -> j.update(issue)));
    }

}
