package com.intellij.myjira.ui.renders;

import static javax.swing.SwingConstants.CENTER;

import com.intellij.myjira.rest.model.JiraIssueTransition;
import com.intellij.myjira.util.JiraLabelUtil;
import com.intellij.util.ui.JBUI;
import java.awt.Component;
import javax.swing.JLabel;
import javax.swing.JList;

public class JiraIssueTransitionListCellRenderer extends DefaultJiraListCellRender {

    private JLabel transitionNameLabel;

    public JiraIssueTransitionListCellRenderer() {
        super();
        init();
    }

    private void init() {
        transitionNameLabel = JiraLabelUtil.createLabel("", CENTER);
        add(transitionNameLabel);
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        JiraIssueTransition transition = (JiraIssueTransition) value;

        setBorder(JBUI.Borders.empty(2));
        transitionNameLabel.setText(transition.getName());


        return this;
    }
}
