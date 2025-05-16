package com.niam.authserver.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Data
@Builder
public class JwtResponse {
    String accessToken;
    String tokenType;
    String refreshToken;
    long expirationTime;
}