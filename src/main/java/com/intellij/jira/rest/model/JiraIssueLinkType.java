package com.intellij.jira.rest.model;

public class JiraIssueLinkType {

    private String id;
    private String name;
    private String self;
    private String inward;
    private String outward;

    public JiraIssueLinkType() {
    }

    public String getInward() {
        return inward;
    }

    public String getOutward() {
        return outward;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSelf() {
        return self;
    }

    public void setSelf(String self) {
        this.self = self;
    }

    public void setInward(String inward) {
        this.inward = inward;
    }

    public void setOutward(String outward) {
        this.outward = outward;
    }
}
