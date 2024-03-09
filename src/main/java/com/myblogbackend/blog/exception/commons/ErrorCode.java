package com.myblogbackend.blog.exception.commons;

import org.springframework.http.HttpStatus;

public enum ErrorCode implements CommonErrorCode {
    SUCCESS(HttpStatus.OK, "Success"),
    FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "System error"),
    ID_NOT_FOUND(HttpStatus.NOT_FOUND, "Could not find the Id"),
    API_NOT_FOUND(HttpStatus.NOT_FOUND, "API Not Found"),
    AUTHORIZATION_FIELD_MISSING(HttpStatus.FORBIDDEN, "Please log in"),
    SIGNATURE_NOT_CORRECT(HttpStatus.FORBIDDEN, "Signature not correct"),
    EXPIRED(HttpStatus.FORBIDDEN, "Expired"),
    UN_SUPPORT_FILE_EXTENSION(HttpStatus.BAD_REQUEST, "Unsupport this file extension"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Unauthorized"),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "validation.error"),
    ALREADY_EXIST(HttpStatus.BAD_REQUEST, "Account already exist!"),
    USER_NAME_PASSWORD_NOT_MATCH(HttpStatus.BAD_REQUEST, "Username or password not match!"),
    USER_ACCOUNT_IS_NOT_ACTIVE(HttpStatus.UNAUTHORIZED, "Account has not active yet"),

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "Account could not found"),
    JWT_CLAIM_EMPTY(HttpStatus.UNAUTHORIZED, "Claim empty"),
    ACCOUNT_NEEDS_TO_VERIFY(HttpStatus.BAD_REQUEST, "Account needs to verify"),
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