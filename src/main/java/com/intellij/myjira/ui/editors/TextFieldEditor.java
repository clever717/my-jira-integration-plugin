package com.intellij.myjira.ui.editors;

import static com.intellij.myjira.util.JiraGsonUtil.createPrimitive;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.components.JBTextField;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.UI;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nullable;

public class TextFieldEditor extends AbstractFieldEditor {

    protected JBTextField myTextField;

    public TextFieldEditor(String fieldName, String issueKey, boolean required) {
        super(fieldName, issueKey, required);
    }

    @Override
    public JComponent createPanel() {
        this.myTextField = new JBTextField();
        this.myTextField.setPreferredSize(UI.size(250, 24));

        return FormBuilder.createFormBuilder()
                .addLabeledComponent(this.myLabel, this.myTextField)
                .getPanel();
    }


    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        return createPrimitive(myTextField.getText());
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && isEmpty(trim(myTextField.getText()))){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required");
        }

        return null;
    }
}
