package com.intellij.jira.ui.editors;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonPrimitive;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;

import javax.swing.*;
import java.util.Map;

import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

public class CustomTextFieldEditor extends AbstractFieldEditor {

    protected JBTextField myTextField;

    public CustomTextFieldEditor(String fieldName, String issueKey) {
        super(fieldName, issueKey);
    }

    @Override
    public JComponent createPanel() {
        this.myTextField = new JBTextField();
        this.myFieldLabel.setLabelFor(this.myTextField);

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myFieldLabel, this.myTextField)
                .getPanel();
    }

    @Override
    public Map<String, String> getInputValues() {
        myInputValues.put(myFieldLabel.getText(), myTextField.getText());
        return myInputValues;
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        return new JsonPrimitive(myTextField.getText());
    }
}