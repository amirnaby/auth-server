package com.niam.authserver.web.exception;

import java.io.Serial;

public final class InvalidPasswordException extends ApplicationException {
    @Serial
    private static final long serialVersionUID = 5861310537366287163L;

    public InvalidPasswordException() {
        super(ErrorRepository.INVALID_PASSWORD);
    }

    public InvalidPasswordException(final String message) {
        super(message, ErrorRepository.INVALID_PASSWORD);
    }
}