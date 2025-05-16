package com.niam.authserver.service;

import com.niam.authserver.web.dto.JwtResponse;
import com.niam.authserver.web.dto.LoginDto;
import com.niam.authserver.web.dto.LogoutDto;
import com.niam.authserver.web.request.TokenRefreshRequest;
import com.niam.authserver.web.response.TokenRefreshResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

public interface LoginHandler {
    JwtResponse authenticate(LoginDto loginDto, HttpServletRequest request);

    void invalidateToken(String username);

    boolean logout(Authentication authentication);

    void validateToken(LogoutDto logoutDto, HttpServletRequest request);

    JwtResponse generateAndCacheToken(UserDetails userDetails);

    TokenRefreshResponse getTokenByRefreshToken(TokenRefreshRequest request);
}