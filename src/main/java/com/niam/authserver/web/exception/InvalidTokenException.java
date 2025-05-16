package com.niam.authserver.web.exception;

public final class InvalidTokenException extends LoginAuthenticationException {
    public InvalidTokenException(String message) {
        super(message, ErrorRepository.INVALID_TOKEN);
    }
}