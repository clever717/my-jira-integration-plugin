package com.intellij.myjira.ui.editors;

import static com.intellij.myjira.util.JiraGsonUtil.createPrimitive;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.openapi.ui.ValidationInfo;
import com.intellij.ui.JBColor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import org.jetbrains.annotations.Nullable;

public class TextAreaFieldEditor extends AbstractFieldEditor {

    private JPanel myPanel;
    private JLabel myTextAreaLabel;
    protected JTextArea myTextArea;


    public TextAreaFieldEditor(String fieldName, String issueKey, boolean required) {
        super(fieldName, issueKey, required);
    }

    @Override
    public JComponent createPanel() {
        this.myTextArea.setBorder(BorderFactory.createLineBorder(JBColor.border()));
        this.myTextAreaLabel.setText(myLabel.getText());

        return myPanel;
    }


    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextArea.getText()))){
            return JsonNull.INSTANCE;
        }

        return createPrimitive(myTextArea.getText());
    }

    @Nullable
    @Override
    public ValidationInfo validate() {
        if(isRequired() && isEmpty(trim(myTextArea.getText()))){
            return new ValidationInfo(myLabel.getMyLabelText() + " is required.");
        }

        return null;
    }

    private void createUIComponents() {
        // TODO: place custom component creation code here
    }
}
