package com.intellij.myjira.ui.panels;

import static java.util.Objects.isNull;

import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.ui.JiraTabbedPane;
import com.intellij.myjira.util.JiraPanelUtil;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JTabbedPane;
import org.jetbrains.annotations.Nullable;

public class JiraIssueDetailsPanel extends SimpleToolWindowPanel {

    private static final String TAB_KEY = "selectedTab";
    private final Map<String, Integer> data = new HashMap<>();
  private JiraIssuesPanel issuesPanel;

  public JiraIssueDetailsPanel(JiraIssuesPanel issuesPanel) {
        super(true);
        setEmptyContent();
    this.issuesPanel = issuesPanel;
    }


    public void showIssue(@Nullable JiraIssue issue) {
        if(isNull(issue)){
            setEmptyContent();
        }else{
            JiraTabbedPane tabbedPane = new JiraTabbedPane(JTabbedPane.BOTTOM);
          tabbedPane.addTab("Preview", new JiraIssuePreviewPanel(issue, issuesPanel));
            tabbedPane.addTab(String.format("Comments (%d)", issue.getComments().getTotal()), new JiraIssueCommentsPanel(issue));
            tabbedPane.addTab(String.format("Links (%d)", issue.getIssueLinks().size()), new JiraIssueLinksPanel(issue.getIssueLinks()));

            tabbedPane.addChangeListener(e -> data.put(TAB_KEY, tabbedPane.getSelectedIndex()));
            tabbedPane.setSelectedIndex(data.getOrDefault(TAB_KEY, 0));

            setContent(tabbedPane);
        }

    }

    public void setEmptyContent(){
        setContent(JiraPanelUtil.createPlaceHolderPanel("Select issue to view details"));
    }

}
