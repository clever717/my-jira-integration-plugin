package com.intellij.myjira.ui.renders;

import static com.intellij.myjira.util.JiraLabelUtil.getBgRowColor;
import static com.intellij.myjira.util.JiraLabelUtil.getFgRowColor;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

public class JiraIssueTableCellRenderer extends DefaultTableCellRenderer {



    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, false, row, column);

        setBackground(getBgRowColor(isSelected));
        setForeground(getFgRowColor(isSelected));

        return this;
    }
}
