package com.intellij.jira.exceptions;

public class InvalidPermissionException extends RuntimeException {

    public InvalidPermissionException(String message, String detailsMessage) {
        super(message, new Throwable(detailsMessage));
    }
}
