package com.niam.authserver.service;

import com.niam.authserver.web.dto.JwtResponse;
import com.niam.authserver.web.dto.LoginDto;
import com.niam.authserver.web.dto.LogoutDto;

import jakarta.servlet.http.HttpServletRequest;

public interface LoginService {
    JwtResponse login(LoginDto loginDto, HttpServletRequest request);

    boolean logout(LogoutDto logoutDto, HttpServletRequest request);
}
