package com.intellij.jira.ui.editors;

import com.intellij.ui.components.JBLabel;

import java.util.HashMap;
import java.util.Map;

import static javax.swing.SwingConstants.RIGHT;

public abstract class AbstractFieldEditor implements FieldEditor {

    protected String issueKey;
    protected JBLabel myFieldLabel;
    protected Map<String, String> myInputValues;

    public AbstractFieldEditor(String fieldName, String issueKey) {
        this.issueKey = issueKey;
        this.myFieldLabel = new JBLabel(fieldName, RIGHT);
        this.myInputValues = new HashMap<>();
    }





}