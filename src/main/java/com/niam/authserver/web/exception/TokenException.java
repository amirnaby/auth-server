package com.niam.authserver.web.exception;

import java.io.Serial;

public final class TokenException extends ApplicationException {
    @Serial
    private static final long serialVersionUID = 5861310537366287163L;


    public TokenException(final String code,final String message) {
        super(message, code);
    }

}
