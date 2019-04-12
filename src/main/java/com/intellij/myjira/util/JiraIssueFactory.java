package com.intellij.myjira.util;

import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.openapi.util.Factory;

@FunctionalInterface
public interface JiraIssueFactory extends Factory<JiraIssue> {

}
