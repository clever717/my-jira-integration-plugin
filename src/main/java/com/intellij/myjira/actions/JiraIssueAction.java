package com.intellij.myjira.actions;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.project.DumbAware;
import javax.swing.JComponent;
import org.jetbrains.annotations.NotNull;

public abstract class JiraIssueAction extends AnAction implements DumbAware {

    private ActionProperties actionProperties;
    private JComponent component;

    public JiraIssueAction(@NotNull ActionProperties actionProperties) {
        super(actionProperties.getText(), actionProperties.getDescription(), actionProperties.getIcon());
        this.actionProperties = actionProperties;

    }

    public void registerComponent(JComponent component){
        this.component = component;
        registerCustomShortcutSet(actionProperties.getShortcut(), component);
    }

    public JComponent getComponent() {
        return component;
    }
}
