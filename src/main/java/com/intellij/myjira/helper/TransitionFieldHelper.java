package com.intellij.myjira.helper;

import static com.intellij.myjira.ui.editors.FieldEditorFactory.createCommentFieldEditor;

import com.google.gson.JsonElement;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssueFieldProperties;
import com.intellij.myjira.ui.editors.FieldEditor;
import com.intellij.myjira.ui.editors.FieldEditorFactory;
import com.intellij.openapi.ui.ValidationInfo;
import javax.swing.JComponent;

public final class TransitionFieldHelper {


    public static FieldEditorInfo createFieldEditorInfo(JiraIssueFieldProperties properties, JiraIssue issueKey){
        return new FieldEditorInfo(properties, issueKey);
    }

    public static FieldEditorInfo createCommentFieldEditorInfo(String issueKey){
        return new FieldEditorInfo("comment", createCommentFieldEditor(issueKey));
    }


    public static class FieldEditorInfo {

        private FieldEditor editor;
        private String name;

        private FieldEditorInfo(JiraIssueFieldProperties properties, JiraIssue issue) {
            this(properties.getSchema().getFieldName(), FieldEditorFactory.create(properties, issue));
        }

        private FieldEditorInfo(String jsonFieldName, FieldEditor fieldEditor) {
            this.name = jsonFieldName;
            this.editor = fieldEditor;
        }

        public JComponent getPanel(){
            return editor.createPanel();
        }

        public JsonElement getJsonValue(){
            return editor.getJsonValue();
        }

        public String getName(){
            return name;
        }

        public boolean isRequired(){
            return editor.isRequired();
        }

        public ValidationInfo validateField(){
            return editor.validate();
        }

    }





}
