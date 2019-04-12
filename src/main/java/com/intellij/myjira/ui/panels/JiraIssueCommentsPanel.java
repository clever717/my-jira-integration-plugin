package com.intellij.myjira.ui.panels;

import static com.intellij.myjira.ui.JiraToolWindowFactory.TOOL_WINDOW_ID;
import static java.awt.BorderLayout.CENTER;
import static javax.swing.ListSelectionModel.SINGLE_SELECTION;
import static javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED;

import com.intellij.myjira.actions.AddCommentDialogAction;
import com.intellij.myjira.actions.DeleteCommentDialogAction;
import com.intellij.myjira.actions.JiraIssueActionGroup;
import com.intellij.myjira.rest.JiraIssueCommentsWrapper;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssueComment;
import com.intellij.myjira.ui.JiraIssueCommentListModel;
import com.intellij.myjira.ui.renders.JiraIssueCommentListCellRenderer;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.ui.ScrollPaneFactory;
import com.intellij.ui.components.JBList;
import com.intellij.ui.components.JBPanel;
import java.awt.BorderLayout;
import java.util.Objects;
import javax.swing.Box;
import javax.swing.SwingUtilities;
import org.jetbrains.annotations.NotNull;

class JiraIssueCommentsPanel extends SimpleToolWindowPanel {

    private JiraIssueCommentsWrapper comments;
    private String issueKey;
    private JiraIssueComment comment;

    private JBList<JiraIssueComment> issueCommentList;

    JiraIssueCommentsPanel(@NotNull JiraIssue issue) {
        super(true);
        this.comments = issue.getComments();
        this.issueKey = issue.getKey();
        initToolbar();
        initContent();
    }

    private void initToolbar() {
        ActionToolbar actionToolbar = ActionManager.getInstance().createActionToolbar(TOOL_WINDOW_ID, createActionGroup(), true);
        actionToolbar.setTargetComponent(this);

        Box toolBarBox = Box.createHorizontalBox();
        toolBarBox.add(actionToolbar.getComponent());
        setToolbar(toolBarBox);
    }

    private ActionGroup createActionGroup() {
        JiraIssueActionGroup group = new JiraIssueActionGroup(this);
        group.add(new AddCommentDialogAction(issueKey));
        group.add(new DeleteCommentDialogAction(issueKey, () -> comment));

        return group;
    }


    private void initContent(){
        JBPanel panel = new JBPanel(new BorderLayout());

        issueCommentList = new JBList<>();
        issueCommentList.setEmptyText("No comments");
        issueCommentList.setModel(new JiraIssueCommentListModel(comments.getComments()));
        issueCommentList.setCellRenderer(new JiraIssueCommentListCellRenderer());
        issueCommentList.setSelectionMode(SINGLE_SELECTION);
        issueCommentList.addListSelectionListener(e -> {
             SwingUtilities.invokeLater(this::updateToolbarActions);
        });

        panel.add(ScrollPaneFactory.createScrollPane(issueCommentList, VERTICAL_SCROLLBAR_AS_NEEDED), CENTER);

        setContent(panel);
    }

    private void updateToolbarActions() {
        JiraIssueComment selectedComment = issueCommentList.getSelectedValue();
        if(!Objects.equals(comment, selectedComment)){
            comment = selectedComment;
            initToolbar();
        }
    }


}
