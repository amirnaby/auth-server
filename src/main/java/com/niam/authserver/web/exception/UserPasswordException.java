package com.niam.authserver.web.exception;

public final class UserPasswordException extends ApplicationException {
    public UserPasswordException(final String code, final String message) {
        super(message, code);
    }
}