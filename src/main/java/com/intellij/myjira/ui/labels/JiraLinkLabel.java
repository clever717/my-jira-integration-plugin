package com.intellij.myjira.ui.labels;


import com.intellij.ide.BrowserUtil;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.JBLabel;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.SwingUtilities;

public class JiraLinkLabel extends JBLabel {

    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    private static final Color LINK_COLOR = JBColor.BLUE;
    private String url;

    public JiraLinkLabel(String text, String url) {
        super(text);
        this.url = url;
        init();
    }

    private void init(){
        setHorizontalAlignment(SwingUtilities.LEFT);
        setToolTipText(this.url);
        setCursor(HAND_CURSOR);
        setForeground(LINK_COLOR);
        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                SwingUtilities.invokeLater(() -> BrowserUtil.open(url));
            }
        });
    }


}
