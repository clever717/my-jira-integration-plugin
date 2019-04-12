package com.intellij.myjira.components;

import com.intellij.myjira.events.JQLSearcherEventListener;
import com.intellij.myjira.rest.model.jql.JQLSearcher;
import com.intellij.myjira.ui.panels.JiraJQLSearcherPanel;
import com.intellij.openapi.components.ProjectComponent;
import java.util.ArrayList;
import java.util.List;

public class JQLSearcherObserver implements ProjectComponent, Updater<JQLSearcher> {


    private List<JQLSearcherEventListener> myListeners;

    @Override
    public void projectOpened() {
        this.myListeners = new ArrayList<>();
    }


    @Override
    public void projectClosed() {
        myListeners.clear();
    }

    public void addListener(JiraJQLSearcherPanel jqlSearcherPanel) {
        myListeners.add(jqlSearcherPanel);
    }


    @Override
    public void update(List<JQLSearcher> jqlSearchers) {
        myListeners.forEach(l -> l.update(jqlSearchers));
    }

    @Override
    public void update(JQLSearcher jqlSearcher) {
        myListeners.forEach(l -> l.update(jqlSearcher));
    }
}
