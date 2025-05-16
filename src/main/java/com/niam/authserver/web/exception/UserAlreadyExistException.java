package com.niam.authserver.web.exception;

import java.io.Serial;

public final class UserAlreadyExistException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 5861310537366287163L;

    public UserAlreadyExistException() {
        super(ErrorRepository.USER_ALREADY_EXIST);
    }

    public UserAlreadyExistException(final String message) {
        super(message, ErrorRepository.USER_ALREADY_EXIST);
    }

}