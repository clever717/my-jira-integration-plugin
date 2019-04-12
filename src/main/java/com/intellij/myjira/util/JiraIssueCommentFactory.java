package com.intellij.myjira.util;

import com.intellij.myjira.rest.model.JiraIssueComment;
import com.intellij.openapi.util.Factory;

@FunctionalInterface
public interface JiraIssueCommentFactory extends Factory<JiraIssueComment> {

}
