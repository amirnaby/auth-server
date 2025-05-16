package com.niam.authserver.web.exception;

import java.io.Serial;

public final class UserNotFoundException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 5861310537366287163L;

    public UserNotFoundException() {
        super(ErrorRepository.USER_NOT_FOUND);
    }

    public UserNotFoundException(final String message) {
        super(message, ErrorRepository.USER_NOT_FOUND);
    }

}