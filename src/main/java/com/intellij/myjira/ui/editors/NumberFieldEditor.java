package com.intellij.myjira.ui.editors;

import static com.intellij.myjira.util.JiraGsonUtil.createPrimitive;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.isNotEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.openapi.ui.ValidationInfo;
import java.util.regex.Pattern;
import org.jetbrains.annotations.Nullable;

public class NumberFieldEditor extends TextFieldEditor {

    private static final Pattern NUMBER_PATTERN = Pattern.compile("\\d+([,.]\\d{1,2})?");

    public NumberFieldEditor(String fieldName, String issueKey, boolean required) {
        super(fieldName, issueKey, required);
    }


    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        return createPrimitive(Double.valueOf(myTextField.getText()));
    }


    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && isEmpty(trim(myTextField.getText()))){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required");
        }

        if(isNotEmpty(trim(myTextField.getText()))){
            if(!NUMBER_PATTERN.matcher(trim(myTextField.getText())).matches()){
                return new ValidationInfo("Wrong format in " + myLabel.getMyLabelText() + " field.");
            }
        }


        return null;
    }
}
