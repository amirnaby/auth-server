package com.niam.authserver.utils;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;

import java.util.Locale;

@Component
@RequiredArgsConstructor
public class MessageUtil {
    private final MessageSource messageSource;
    private final Locale defaultLocale;

    public String getMessage(String message) {
        return messageSource.getMessage(message, new Object[0], new Locale(defaultLocale.getLanguage()));
    }

    public String getMessage(String message, String... args) {
        return messageSource.getMessage(message, args, new Locale(defaultLocale.getLanguage()));
    }
}