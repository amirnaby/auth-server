package com.niam.authserver.web.exception;

public class ApplicationException extends RuntimeException {
    private final String code;
    public ApplicationException(String message, String code) {
        super(message);
        this.code = code;
    }
    public ApplicationException(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}
