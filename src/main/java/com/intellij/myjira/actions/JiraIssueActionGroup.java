package com.intellij.myjira.actions;

import com.intellij.openapi.actionSystem.DefaultActionGroup;
import javax.swing.JComponent;

public class JiraIssueActionGroup extends DefaultActionGroup {

    private JComponent parent;

    public JiraIssueActionGroup(JComponent component) {
        super();
        this.parent = component;
    }

    public void add(JiraIssueAction action){
        action.registerComponent(parent);
        super.add(action);
    }

    @Override
    public boolean isDumbAware() {
        return true;
    }
}
