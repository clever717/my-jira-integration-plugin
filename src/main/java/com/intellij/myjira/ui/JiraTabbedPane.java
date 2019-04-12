package com.intellij.myjira.ui;

import com.intellij.ui.components.JBTabbedPane;
import com.intellij.util.ui.JBUI;
import java.awt.Insets;
import org.jetbrains.annotations.NotNull;

public class JiraTabbedPane extends JBTabbedPane {


    public JiraTabbedPane(int tabPlacement) {
        super(tabPlacement);
    }


    @NotNull
    @Override
    protected Insets getInsetsForTabComponent() {
        return JBUI.insets(0);
    }
}
