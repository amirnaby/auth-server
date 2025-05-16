package com.niam.authserver.web.exception;

import org.springframework.security.core.AuthenticationException;

public class LoginAuthenticationException extends AuthenticationException {
    private final String code;

    public LoginAuthenticationException(String msg, String code) {
        super(msg);
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}