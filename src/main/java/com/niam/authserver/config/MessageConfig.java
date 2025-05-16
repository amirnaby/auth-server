package com.niam.authserver.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

import java.util.Locale;

/**
 * @Author A.Fallah
 * @Date 2023/11/07
 * @since version 1.0.0
 * @Email Fallah.Arsalan@gmail.com *
 */
@Configuration
public class MessageConfig implements WebMvcConfigurer{
    @Value("${defaultLocale.language}")
    private String defaultLang;
    @Value("${defaultLocale.country}")
    private String defaultCountry;

    @Bean("messageSource")
    public ResourceBundleMessageSource messageSource() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("language/messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setDefaultLocale(defaultLocale());
        return messageSource;
    }

    @Bean
    public Locale defaultLocale() {
        return new Locale(defaultLang, defaultCountry);
    }

    @Bean
    public LocaleChangeInterceptor localeChangeInterceptor() {
        LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
        localeChangeInterceptor.setParamName("language");
        return localeChangeInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(localeChangeInterceptor());
    }

}