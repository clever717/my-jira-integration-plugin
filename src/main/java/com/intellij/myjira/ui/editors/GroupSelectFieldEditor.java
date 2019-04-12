package com.intellij.myjira.ui.editors;

import static com.intellij.myjira.util.JiraGsonUtil.createArrayNameObjects;
import static com.intellij.myjira.util.JiraGsonUtil.createNameObject;
import static com.intellij.openapi.util.text.StringUtil.isEmpty;
import static com.intellij.openapi.util.text.StringUtil.trim;
import static com.intellij.util.containers.ContainerUtil.getFirstItem;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.intellij.myjira.rest.model.JiraGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import java.util.ArrayList;
import java.util.List;
import org.jetbrains.annotations.Nullable;

public class GroupSelectFieldEditor extends SelectFieldEditor {

    private List<String> selectedGroups = new ArrayList<>();

    public GroupSelectFieldEditor(String fieldName, String issueKey, boolean required, boolean isMultiSelect) {
        super(fieldName, issueKey, required, isMultiSelect);
        myButtonAction = new GroupPickerDialogAction();
    }

    @Override
    public JsonElement getJsonValue() {
        if(isEmpty(trim(myTextField.getText()))){
            return JsonNull.INSTANCE;
        }

        if(isMultiSelect){
            return createArrayNameObjects(selectedGroups);
        }

        return createNameObject(getFirstItem(selectedGroups));
    }


    private class GroupPickerDialogAction extends PickerDialogAction {

        public GroupPickerDialogAction() {
            super();
        }

        @Override
        public void actionPerformed(AnActionEvent e) {
            super.actionPerformed(e);
            if(nonNull(myJiraRestApi)){
                List<String> users = myJiraRestApi.getGroups().stream().map(JiraGroup::getName).collect(toList());
                GroupPickerDialog dialog = new GroupPickerDialog(myProject, users);
                dialog.show();
            }

        }
    }


    class GroupPickerDialog extends PickerDialog<String> {

        public GroupPickerDialog(@Nullable Project project, List<String> items) {
            super(project, "Groups", items);
        }



        @Override
        protected void doOKAction() {
            selectedGroups = myList.getSelectedValuesList();
            myTextField.setText(selectedGroups.isEmpty() ? "" : String.join(", ", selectedGroups));


            super.doOKAction();
        }
    }





}
