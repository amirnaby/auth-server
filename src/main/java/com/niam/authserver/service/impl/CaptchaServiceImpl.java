package com.niam.authserver.service.impl;

import cn.apiclub.captcha.Captcha;
import com.niam.authserver.service.CacheService;
import com.niam.authserver.service.CaptchaService;
import com.niam.authserver.utils.CaptchaUtil;
import com.niam.commonservice.utils.MessageUtil;
import com.niam.authserver.utils.CacheRepository;
import com.niam.authserver.web.dto.CaptchaDetails;
import com.niam.authserver.web.exception.CaptchaIsWrongException;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.Cache;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CaptchaServiceImpl implements CaptchaService {
    private final CacheService cacheService;
    private final MessageUtil messageUtil;

    @Override
    public void validate(String token, String input) {
        Cache captchaCache = cacheService.getCache(CacheRepository.CAPTCHA_CACHE);
        String cachedInput = captchaCache.get(token, String.class);
        captchaCache.evictIfPresent(token);
        if (input == null || !input.equals(cachedInput)) {
            throw new CaptchaIsWrongException(messageUtil.getMessage("message.invalidReCaptcha"));
        }
    }

    @Override
    public CaptchaDetails generateCaptcha() {
        Cache captchaCache = cacheService.getCache(CacheRepository.CAPTCHA_CACHE);
        Captcha captcha = CaptchaUtil.createCaptcha(240, 70);
        String captchaStr = "data:realCaptcha/jpg;base64," + CaptchaUtil.encodeCaptcha(captcha);
        String uuid = UUID.randomUUID().toString();
        CaptchaDetails captchaDetails = new CaptchaDetails(uuid, captchaStr);
        captchaCache.put(uuid, captcha.getAnswer());
        return captchaDetails;
    }
}