package com.niam.authserver.service.impl;

import com.niam.authserver.service.CaptchaService;
import com.niam.authserver.service.LoginHandler;
import com.niam.authserver.service.LoginService;
import com.niam.authserver.web.dto.JwtResponse;
import com.niam.authserver.web.dto.LoginDto;
import com.niam.authserver.web.dto.LogoutDto;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {
    private final CaptchaService captchaService;
    private final LoginHandler loginHandler;
    @Override
    public JwtResponse login(LoginDto loginDto, HttpServletRequest request) {
        loginHandler.invalidateToken(loginDto.getUsername());
        captchaService.validate(loginDto.getToken(), loginDto.getInput());
        return loginHandler.authenticate(loginDto, request);
    }

    @Override
    public boolean logout(LogoutDto logoutDto, HttpServletRequest request) {
        loginHandler.validateToken(logoutDto, request);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return loginHandler.logout(authentication);
    }
}
