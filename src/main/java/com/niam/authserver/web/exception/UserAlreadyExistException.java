package com.niam.authserver.web.exception;

public final class UserAlreadyExistException extends ApplicationException {
    public UserAlreadyExistException() {
        super(ErrorRepository.USER_ALREADY_EXIST);
    }

    public UserAlreadyExistException(final String message) {
        super(message, ErrorRepository.USER_ALREADY_EXIST);
    }
}