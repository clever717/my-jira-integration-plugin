package com.intellij.myjira.ui.table;

import com.intellij.myjira.helper.ColumnInfoHelper;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.util.ui.ListTableModel;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class JiraIssueListTableModel extends ListTableModel<JiraIssue> {

    public JiraIssueListTableModel(@NotNull List<JiraIssue> jiraIssues) {
        super();
        setColumnInfos(ColumnInfoHelper.getHelper().generateColumnsInfo());
        setItems(jiraIssues);
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }


}
