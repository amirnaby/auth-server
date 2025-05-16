package com.niam.authserver.web.exception;

public final class UserNotFoundException extends ApplicationException {
    public UserNotFoundException() {
        super(ErrorRepository.USER_NOT_FOUND);
    }

    public UserNotFoundException(final String message) {
        super(message, ErrorRepository.USER_NOT_FOUND);
    }
}