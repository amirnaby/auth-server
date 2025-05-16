package com.niam.authserver.web.exception;

import java.io.Serial;

public final class UserPasswordException extends ApplicationException {

    @Serial
    private static final long serialVersionUID = 5861310537366287263L;
    public UserPasswordException(final String code,final String message) {
        super(message, code);
    }

}