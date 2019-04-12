package com.intellij.myjira.ui.editors;

import com.google.gson.JsonElement;
import com.intellij.openapi.ui.ValidationInfo;
import javax.swing.JComponent;
import org.jetbrains.annotations.Nullable;

public interface FieldEditor {

     JComponent createPanel();

     JsonElement getJsonValue();

     boolean isRequired();

     @Nullable
     ValidationInfo validate();

}
