package com.niam.authserver.service;

import com.niam.authserver.web.dto.JwtResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface JwtHandler {
    String validateJwtAndGetUsername(String jwt);

    void isAlreadyLoggedInWithJwt(String username, String token);

    JwtResponse generateJwtResponse(UserDetails userDetails);

    String generateTokenFromUsername(UserDetails userDetails);

    int deleteByUserName(String username);
}
