package com.niam.authserver.web.exception;

public final class UserMustChangePasswordException extends ApplicationException {
    public UserMustChangePasswordException(final String code, final String message) {
        super(message, code);
    }
}