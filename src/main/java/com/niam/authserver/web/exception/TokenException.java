package com.niam.authserver.web.exception;

public final class TokenException extends ApplicationException {
    public TokenException(final String code, final String message) {
        super(message, code);
    }
}