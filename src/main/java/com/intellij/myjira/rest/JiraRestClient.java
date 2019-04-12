package com.intellij.myjira.rest;

import static com.intellij.myjira.rest.JiraIssueParser.parseGroups;
import static com.intellij.myjira.rest.JiraIssueParser.parseIssue;
import static com.intellij.myjira.rest.JiraIssueParser.parseIssueComment;
import static com.intellij.myjira.rest.JiraIssueParser.parseIssueLinkTypes;
import static com.intellij.myjira.rest.JiraIssueParser.parseIssuePriorities;
import static com.intellij.myjira.rest.JiraIssueParser.parseIssueTransitions;
import static com.intellij.myjira.rest.JiraIssueParser.parseIssues;
import static com.intellij.myjira.rest.JiraIssueParser.parseProject;
import static com.intellij.myjira.rest.JiraIssueParser.parseProjectVersionsDetail;
import static com.intellij.myjira.rest.JiraIssueParser.parseProjectVersionsDetails;
import static com.intellij.myjira.rest.JiraIssueParser.parseProjects;
import static com.intellij.myjira.rest.JiraIssueParser.parseUser;
import static com.intellij.myjira.rest.JiraIssueParser.parseUsers;
import static com.intellij.myjira.util.JiraGsonUtil.createIdObject;
import static java.util.Objects.nonNull;

import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.intellij.myjira.helper.TransitionFieldHelper.FieldEditorInfo;
import com.intellij.myjira.rest.model.JiraGroup;
import com.intellij.myjira.rest.model.JiraIssue;
import com.intellij.myjira.rest.model.JiraIssueComment;
import com.intellij.myjira.rest.model.JiraIssueForCreate;
import com.intellij.myjira.rest.model.JiraIssueLinkType;
import com.intellij.myjira.rest.model.JiraIssuePriority;
import com.intellij.myjira.rest.model.JiraIssueTransition;
import com.intellij.myjira.rest.model.JiraIssueUser;
import com.intellij.myjira.rest.model.JiraPermission;
import com.intellij.myjira.rest.model.JiraProject;
import com.intellij.myjira.rest.model.JiraProjectVersionDetails;
import com.intellij.tasks.jira.JiraRepository;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.DeleteMethod;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;

public class JiraRestClient {
    private static final Integer MAX_ISSUES_RESULTS = 500;
    private static final Integer MAX_USERS_RESULTS = 200;

    private static final String MYSELF = "myself";
    private static final String ISSUE = "issue";
    private static final String ISSUE_LINK = "issueLink";
    private static final String TRANSITIONS = "transitions";
    private static final String SEARCH = "search";

    private JiraRepository jiraRepository;

    public JiraRestClient(JiraRepository jiraRepository) {
        this.jiraRepository = jiraRepository;
    }

    public JiraIssueUser getMyInfo() throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(MYSELF));
        String response = jiraRepository.executeMethod(method);
        return parseUser(response);
    }
    public JiraIssue getIssue(String issueIdOrKey) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(ISSUE, issueIdOrKey));
        method.setQueryString(method.getQueryString() + "?fields=" + JiraIssue.REQUIRED_FIELDS);
        String response = jiraRepository.executeMethod(method);
        return parseIssue(response);
    }

    public List<JiraIssue> findIssues(String searchQuery) throws Exception {
        GetMethod method = getBasicSearchMethod(searchQuery, MAX_ISSUES_RESULTS);
        method.setQueryString(method.getQueryString() + "&fields=" + JiraIssue.REQUIRED_FIELDS);
        String response = jiraRepository.executeMethod(method);
        return parseIssues(response);
    }

    public List<JiraIssue> findIssues() throws Exception {
        return findIssues(this.jiraRepository.getSearchQuery());
    }


    public List<JiraIssueTransition> getTransitions(String issueId) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(ISSUE, issueId, TRANSITIONS));
        method.setQueryString(new NameValuePair[]{new NameValuePair("expand", "transitions.fields")});
        String response = jiraRepository.executeMethod(method);
        return parseIssueTransitions(response);
    }


    public String transitIssue(String issueId, String transitionId, Map<String, FieldEditorInfo> fields) throws Exception {
        String requestBody = getTransitionRequestBody(transitionId, fields);
        PostMethod method = new PostMethod(this.jiraRepository.getRestUrl(ISSUE, issueId, TRANSITIONS));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }

    public List<JiraIssueUser> getAssignableUsers(String type, String key) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("user", "assignable", SEARCH));

        List<JiraIssueUser> newUsers;
        List<JiraIssueUser> jiraUsers = new ArrayList<>();

        do {
            if ("project".equals(type)) {
                method.setQueryString(new NameValuePair[]{
                        new NameValuePair("project", key),
                        new NameValuePair("startAt", String.valueOf(jiraUsers.size())),
                        new NameValuePair("maxResults", String.valueOf(MAX_USERS_RESULTS))
                });
            } else {
                method.setQueryString(new NameValuePair[]{
                        new NameValuePair("issueKey", key),
                        new NameValuePair("startAt", String.valueOf(jiraUsers.size())),
                        new NameValuePair("maxResults", String.valueOf(MAX_USERS_RESULTS))
                });
            }


            String response = jiraRepository.executeMethod(method);
            newUsers = parseUsers(response);
            jiraUsers.addAll(newUsers);
        } while (newUsers.size() == MAX_USERS_RESULTS);

        return jiraUsers;
    }


    public String assignUserToIssue(String username, String issueKey) throws Exception {
        String requestBody = "{\"name\": \"" + username + "\"}";
        PutMethod method = new PutMethod(this.jiraRepository.getRestUrl(ISSUE, issueKey, "assignee"));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }


    public JiraIssueComment addCommentToIssue(String body, String issueKey) throws Exception {
        String requestBody = "{\"body\": \"" + body + "\"}";
        PostMethod method = new PostMethod(this.jiraRepository.getRestUrl(ISSUE, issueKey, "comment"));
        method.setRequestEntity(createJsonEntity(requestBody));
        String response = jiraRepository.executeMethod(method);
        return parseIssueComment(response);
    }


    private GetMethod getBasicSearchMethod(String jql, int maxResults) {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl(SEARCH));
        method.setQueryString(new NameValuePair[]{new NameValuePair("jql", jql), new NameValuePair("maxResults", String.valueOf(maxResults))});
        return method;
    }


    private static RequestEntity createJsonEntity(String requestBody) {
        try {
            return new StringRequestEntity(requestBody, "application/json", "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new AssertionError("UTF-8 encoding is not supported");
        }
    }


    public String deleteCommentToIssue(String issueKey, String commentId) throws Exception {
        DeleteMethod method = new DeleteMethod(this.jiraRepository.getRestUrl(ISSUE, issueKey, "comment", commentId));
        return jiraRepository.executeMethod(method);
    }

    public List<JiraIssuePriority> getIssuePriorities() throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("priority"));
        String response = jiraRepository.executeMethod(method);
        return parseIssuePriorities(response);
    }

    public String changeIssuePriority(String priorityName, String issueIdOrKey) throws Exception {
        String requestBody = "{\"update\": {\"priority\": [{\"set\": {\"name\": \"" + priorityName + "\"}}]}}";
        PutMethod method = new PutMethod(this.jiraRepository.getRestUrl(ISSUE, issueIdOrKey));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }


    public List<JiraIssueUser> findUsersWithPermissionOnIssue(String issueKey, JiraPermission permission) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("user", "permission", SEARCH));
        method.setQueryString(new NameValuePair[]{new NameValuePair("issueKey", issueKey), new NameValuePair("username", jiraRepository.getUsername()), new NameValuePair("permissions", permission.toString())});
        String response = jiraRepository.executeMethod(method);
        return parseUsers(response);
    }


    public List<JiraIssueLinkType> getIssueLinkTypes() throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("issueLinkType"));
        String response = jiraRepository.executeMethod(method);
        return parseIssueLinkTypes(response);
    }

    public List<JiraGroup> getGroups() throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("groups", "picker"));
        String response = jiraRepository.executeMethod(method);
        return parseGroups(response);
    }



    private String getTransitionRequestBody(String transitionId, Map<String, FieldEditorInfo> fields) {
        JsonObject transition = new JsonObject();
        transition.add("transition", createIdObject(transitionId));

        // Update
        JsonObject updateObject = new JsonObject();

        // Comment
        FieldEditorInfo commentField = fields.remove("comment");
        if(nonNull(commentField) && !(commentField.getJsonValue() instanceof JsonNull)){
            updateObject.add("comment", commentField.getJsonValue());
        }

        // Linked Issues
        FieldEditorInfo issueLinkField = fields.remove("issuelinks");
        if(nonNull(issueLinkField) && !(issueLinkField.getJsonValue() instanceof JsonNull)){
            updateObject.add("issuelinks", issueLinkField.getJsonValue());
        }

        if(updateObject.size() > 0){
            transition.add("update", updateObject);
        }

        //Fields
        JsonObject fieldsObject = new JsonObject();
        fields.forEach((key, value) -> {
            if(!(value.getJsonValue() instanceof JsonNull)){
                fieldsObject.add(key, value.getJsonValue());
            }
        });

        if(fieldsObject.size() > 0){
            transition.add("fields", fieldsObject);
        }


        return transition.toString();
    }


    public String getDefaultSearchQuery() {
        return jiraRepository.getSearchQuery();
    }

    public boolean testConnection() throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("myself"));
        jiraRepository.executeMethod(method);
        return method.getStatusCode() == 200;
    }

    public List<JiraProject> findProjects() throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("project"));
        String response = jiraRepository.executeMethod(method);
        return parseProjects(response);
    }

    public JiraProject getProject(String projectKey) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("project", projectKey));
        String response = jiraRepository.executeMethod(method);
        return parseProject(response);
    }

    public JiraProjectVersionDetails addVersionToProject(String projectName, String projectId, String name, String description) throws Exception {
        String requestBody = "{\"description\":\"" + description + "\",\"name\":\"" + name + "\",\"archived\":false,\"released\":false,\"project\":\"" + projectName + "\",\"projectId\":" + projectId + "}";
        PostMethod method = new PostMethod(this.jiraRepository.getRestUrl("version"));
        method.setRequestEntity(createJsonEntity(requestBody));
        String response = jiraRepository.executeMethod(method);
        return parseProjectVersionsDetail(response);
    }

    public List<JiraProjectVersionDetails> getProjectVersionDetails(String projectKey) throws Exception {
        GetMethod method = new GetMethod(this.jiraRepository.getRestUrl("project", projectKey, "versions"));
        String response = jiraRepository.executeMethod(method);
        return parseProjectVersionsDetails(response);
    }

    public String changeIssueFixVersion(String version, String issueIdOrKey) throws Exception {
        String requestBody = "{\"update\": {\"fixVersions\": [{\"set\": [{\"name\": \"" + version + "\"}]}]}}";
        PutMethod method = new PutMethod(this.jiraRepository.getRestUrl(ISSUE, issueIdOrKey));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }

    public JiraIssue createIssue(JiraIssueForCreate issue) throws Exception {
        String requestBody = "{\"fields\":{\"project\":{\"id\":\"" + issue.getProjectId() + "\"},\"summary\":\"" + issue.getSummary() + "\",\"issuetype\":{\"id\":\"" + issue.getIssueTypeId() + "\"},\"assignee\":{\"name\":\"" + issue.getAssignee() + "\"},\"reporter\":{\"name\":\"" + issue.getReporter() + "\"},\"description\":\"" + issue.getDescription().replace("\r", "").replace("\n", "\\r\\n") + "\"}}";
        PostMethod method = new PostMethod(this.jiraRepository.getRestUrl(ISSUE));
        method.setRequestEntity(createJsonEntity(requestBody));
        String response = jiraRepository.executeMethod(method);
        return parseIssue(response);
    }

    public String linkIssue(String issueIdOrKey, String parentIssueIdOrKey) throws Exception {
        String requestBody = "{\"type\":{\"name\":\"관련이슈\"},\"inwardIssue\":{\"key\":\"" + parentIssueIdOrKey + "\"},\"outwardIssue\":{\"key\":\"" + issueIdOrKey + "\"}}}";
        PostMethod method = new PostMethod(this.jiraRepository.getRestUrl(ISSUE_LINK));
        method.setRequestEntity(createJsonEntity(requestBody));
        return jiraRepository.executeMethod(method);
    }
}

