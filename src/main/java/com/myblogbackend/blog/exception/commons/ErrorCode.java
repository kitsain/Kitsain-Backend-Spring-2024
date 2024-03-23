package com.myblogbackend.blog.exception.commons;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements CommonErrorCode {
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Could not find the Id"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Account already exist!"),
    USER_ACCOUNT_IS_NOT_ACTIVE(HttpStatus.UNAUTHORIZED, "Account has not active yet"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Account could not found"),
    INVALID_ACCESS_TOKEN(HttpStatus.BAD_REQUEST, "Invalid access token"),
    PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Could not found comment parent"),
    UNABLE_EDIT_COMMENT(HttpStatus.UNAUTHORIZED, "Unable to edit comment"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Could not found comment"),
    EMAIL_SEND_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "error.could not send email");
    private final HttpStatus status;
    private final String message;

    private ErrorCode(final HttpStatus status, final String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus status() {
        return this.status;
    }

    public String message() {
        return this.message;
    }
}