package com.intellij.myjira.ui.renders;

import static com.intellij.myjira.util.JiraLabelUtil.getBgRowColor;

import com.intellij.myjira.util.JiraIconUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.ui.table.IconTableCellRenderer;
import java.awt.Component;
import javax.swing.Icon;
import javax.swing.JTable;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JiraIconAndTextTableCellRenderer extends IconTableCellRenderer {
    private String iconUrl;
    private String label;

    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

    public void setLabel(String label) {
        this.label = StringUtil.isNotEmpty(label) ? label : "";
    }

    public void emptyText(){
        this.label = "";
    }

    @Nullable
    @Override
    protected Icon getIcon(@NotNull Object value, JTable table, int row) {
        return JiraIconUtil.getSmallIcon(iconUrl);
    }

    @Override
    protected boolean isCenterAlignment() {
        return true;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean selected, boolean focus, int row, int column) {
        super.getTableCellRendererComponent(table, value, selected, false, row, column);
        setText(label);
        setBackground(getBgRowColor(selected));

        return this;
    }

}
