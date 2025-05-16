package com.niam.authserver.service;

import com.niam.authserver.web.dto.CaptchaDetails;

public interface CaptchaService {
    void validate(String token, String input);

    CaptchaDetails generateCaptcha();
}
