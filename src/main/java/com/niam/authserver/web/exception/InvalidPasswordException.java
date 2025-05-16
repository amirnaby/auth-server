package com.niam.authserver.web.exception;

public final class InvalidPasswordException extends ApplicationException {
    public InvalidPasswordException() {
        super(ErrorRepository.INVALID_PASSWORD);
    }

    public InvalidPasswordException(final String message) {
        super(message, ErrorRepository.INVALID_PASSWORD);
    }
}