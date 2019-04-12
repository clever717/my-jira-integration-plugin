package com.intellij.myjira.ui.editors;

import static com.intellij.myjira.util.JiraGsonUtil.createObject;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;

public class CommentFieldEditor extends TextAreaFieldEditor {

    public CommentFieldEditor(String issueKey) {
        super("Comment", issueKey, false);
    }


    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextArea.getText()))){
            return JsonNull.INSTANCE;
        }

        JsonArray array = new JsonArray();
        JsonObject addObject = new JsonObject();
        addObject.add("add", createObject("body", myTextArea.getText()));
        array.add(addObject);

        return array;
    }
}
