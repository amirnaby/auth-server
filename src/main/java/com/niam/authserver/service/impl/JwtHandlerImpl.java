package com.niam.authserver.service.impl;

import com.niam.authserver.persistence.dao.RefreshTokenRepository;
import com.niam.authserver.persistence.dao.UserRepository;
import com.niam.authserver.persistence.model.User;
import com.niam.authserver.service.CacheService;
import com.niam.authserver.service.JwtHandler;
import com.niam.authserver.utils.CacheRepository;
import com.niam.authserver.web.dto.JwtResponse;
import com.niam.authserver.web.exception.InvalidTokenException;
import com.niam.commonservice.utils.MessageUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtHandlerImpl implements JwtHandler {
    private final MessageUtil messageUtil;
    private final CacheService cacheService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    @Value("${auth-server.security.jwt-refresh-expiration-second}")
    private Long refreshTokenDuration;
    @Value("${auth-server.security.jwt-secret}")
    private String jwtSecret;
    @Value("${auth-server.security.jwt-expiration-second}")
    private Long jwtExpiration;

    @Override
    public String validateJwtAndGetUsername(String token) {
        Assert.notNull(token, messageUtil.getMessage("invalid.token.exception"));
        token = token.replace("Bearer ", "");
        Jws<Claims> jws = buildJwtIfValid(token);
        return jws.getBody().getSubject();
    }

    @Override
    public void isAlreadyLoggedInWithJwt(String username, String token) {
        Cache tokenCache = cacheService.getCache(CacheRepository.TOKEN_CACHE);
        JwtResponse jwtResponse = tokenCache.get(username, JwtResponse.class);
        token = token.replace("Bearer ", "");
        if (jwtResponse == null || !jwtResponse.getAccessToken().equals(token))
            throw new InvalidTokenException(
                    messageUtil.getMessage("auth.message.invalidToken"));
    }

    @Override
    public JwtResponse generateJwtResponse(UserDetails userDetails) {
        String jwt = generateTokenFromUsername(userDetails);
        return new JwtResponse(jwt, "Bearer", null, jwtExpiration);
    }

    private Jws<Claims> buildJwtIfValid(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key())
                .build()
                .parseClaimsJws(token);
    }

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public String generateTokenFromUsername(UserDetails userDetails) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("auth-server")
                .setSubject(userDetails.getUsername())
                .claim("roles", userDetails.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + (jwtExpiration * 1000)))
                .signWith(key())
                .compact();
    }

    @Transactional("transactionManager")
    public int deleteByUserName(String username) {
        User user = userRepository.findByUsername(username);
        return refreshTokenRepository.deleteByUser(userRepository.findById(user.getId()).isPresent()
                ? userRepository.findById(user.getId()).get() : null);
    }
}