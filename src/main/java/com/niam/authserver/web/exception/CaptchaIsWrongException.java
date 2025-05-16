package com.niam.authserver.web.exception;

public final class CaptchaIsWrongException extends LoginAuthenticationException {
    public CaptchaIsWrongException(String message) {
        super(message, ErrorRepository.CAPTCHA_IS_WRONG);
    }
}