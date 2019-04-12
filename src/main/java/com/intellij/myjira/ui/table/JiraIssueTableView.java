package com.intellij.myjira.ui.table;

import static javax.swing.ListSelectionModel.SINGLE_SELECTION;

import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.ui.table.TableView;
import java.awt.Dimension;
import java.util.List;
import javax.swing.table.TableColumnModel;

public class JiraIssueTableView extends TableView<JiraIssue> {

    private JiraIssueListTableModel model;

    public JiraIssueTableView(List<JiraIssue> issues) {
        super();
        model = new JiraIssueListTableModel(issues);
        setModelAndUpdateColumns(model);
        setSelectionMode(SINGLE_SELECTION);
        setIntercellSpacing(new Dimension());
        setShowGrid(false);
        setRowHeight(25);
    }


    @Override
    protected TableColumnModel createDefaultColumnModel() {
        TableColumnModel columnModel = super.createDefaultColumnModel();
        columnModel.setColumnMargin(0);
        return columnModel;
    }

    public void updateModel(List<JiraIssue> issues){
        model.setItems(issues);
    }

    @Override
    public JiraIssueListTableModel getModel() {
        return model;
    }
}
