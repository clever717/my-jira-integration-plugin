package com.intellij.jira.rest.model;

import com.google.gson.annotations.SerializedName;

import java.util.Objects;

public class JiraIssueUser {

    private String self;
    private String name;
    private String key;
    private String emailAdress;
    private String displayName;
    private JiraIssueUser.Avatar avatarUrls;

    public JiraIssueUser() { }

    public JiraIssueUser(String self, String name, String key, String emailAdress, String displayName, JiraIssueUser.Avatar avatarUrls) {
        this.self = self;
        this.name = name;
        this.key = key;
        this.emailAdress = emailAdress;
        this.displayName = displayName;
        this.avatarUrls = avatarUrls;
    }

    public JiraIssueUser(JiraIssueUser other) {
        this(other.getSelf(), other.getName(), other.getKey(), other.getEmailAdress(), other.getDisplayName(), other.getAvatarUrls());
    }

    public String getSelf() {
        return self;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public String getEmailAdress() {
        return emailAdress;
    }

    public String getDisplayName() {
        return displayName;
    }

    public Avatar getAvatarUrls() {
        return avatarUrls;
    }

    public String getAvatarIcon(){
        return avatarUrls.xsmallIcon;
    }

    public class Avatar{

        @SerializedName("16x16")
        private String xsmallIcon;
        @SerializedName("24x24")
        private String smallIcon;
        @SerializedName("32x32")
        private String mediumIcon;
        @SerializedName("48x48")
        private String largeIcon;

        public Avatar() { }

    }

    @Override
    public JiraIssueUser clone() {
        return new JiraIssueUser(this);
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        JiraIssueUser that = (JiraIssueUser) o;
        return Objects.equals(key, that.key);
    }
}
