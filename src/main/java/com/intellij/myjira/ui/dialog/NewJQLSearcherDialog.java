package com.intellij.myjira.ui.dialog;

import static java.util.Objects.nonNull;

import com.intellij.myjira.components.JQLSearcherManager;
import com.intellij.myjira.rest.model.jql.JQLSearcher;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class NewJQLSearcherDialog extends EditJQLSearcherDialog {

    public NewJQLSearcherDialog(@NotNull Project project) {
        this(project, true);
    }

    public NewJQLSearcherDialog(@NotNull Project project, boolean applyOkAction) {
        super(project, new JQLSearcher(), false, applyOkAction);
        setTitle("New JQL Searcher");
    }


    @Override
    protected void doOKAction() {
        myEditor.apply();
        if(myApplyOkAction && nonNull(myProject)){
            JQLSearcherManager jqlManager = getJqlSearcherManager();
            jqlManager.add(mySearcher, myEditor.isSelectedSearcher());
        }

        close(0);
    }






}
