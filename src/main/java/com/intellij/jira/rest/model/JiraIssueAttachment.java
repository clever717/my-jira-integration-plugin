package com.intellij.jira.rest.model;

import java.util.Date;

public class JiraIssueAttachment {

  private String id;
  private String filename;
  private String size;
  private String mimeType;
  private String content;
  private Date created;

  public String getId() {
    return id;
  }

  public String getFilename() {
    return filename;
  }

  public String getSize() {
    return size;
  }

  public String getMimeType() {
    return mimeType;
  }

  public String getContent() {
    return content;
  }

  public Date getCreated() {
    return created;
  }
}
