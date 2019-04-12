package com.intellij.myjira.ui;

import com.intellij.myjira.rest.model.JiraIssueLinkType;
import com.intellij.myjira.rest.model.JiraIssueLinkTypeInfo;
import java.util.ArrayList;
import java.util.List;
import javax.swing.AbstractListModel;

public class JiraIssueLinkTypeInfoListModel  extends AbstractListModel<JiraIssueLinkTypeInfo> {

    private List<JiraIssueLinkTypeInfo> issueLinkTypes = new ArrayList<>();

    public JiraIssueLinkTypeInfoListModel(List<JiraIssueLinkType> issueLinkTypes) {
        issueLinkTypes.forEach(type -> {
            this.issueLinkTypes.add(JiraIssueLinkTypeInfo.inward(type));
            this.issueLinkTypes.add(JiraIssueLinkTypeInfo.outward(type));
        });

    }

    @Override
    public int getSize() {
        return issueLinkTypes.size();
    }

    @Override
    public JiraIssueLinkTypeInfo getElementAt(int index) {
        return issueLinkTypes.get(index);
    }
}
