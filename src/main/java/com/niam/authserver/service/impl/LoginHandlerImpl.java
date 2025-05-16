package com.niam.authserver.service.impl;

import com.niam.authserver.persistence.dao.RefreshTokenRepository;
import com.niam.authserver.persistence.dao.UserRepository;
import com.niam.authserver.persistence.model.RefreshToken;
import com.niam.authserver.service.CacheService;
import com.niam.authserver.service.JwtHandler;
import com.niam.authserver.service.LoginHandler;
import com.niam.authserver.utils.MessageUtil;
import com.niam.authserver.utils.SmsPanelRepository;
import com.niam.authserver.web.dto.JwtResponse;
import com.niam.authserver.web.dto.LoginDto;
import com.niam.authserver.web.dto.LogoutDto;
import com.niam.authserver.web.exception.ResultResponseStatus;
import com.niam.authserver.web.exception.TokenException;
import com.niam.authserver.web.exception.UserNotFoundException;
import com.niam.authserver.web.request.TokenRefreshRequest;
import com.niam.authserver.web.response.TokenRefreshResponse;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.Cache;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class LoginHandlerImpl implements LoginHandler {
    private final AuthenticationManager authenticationManager;
    private final JwtHandler jwtHandler;
    private final CacheService cacheService;
    private final MessageUtil messageUtil;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    @Value("${auth-server.security.jwt-refresh-expiration-second}")
    private Long refreshTokenDuration;
    @Value("${auth-server.security.jwt-secret}")
    private String jwtSecret;

    @Override
    public JwtResponse authenticate(LoginDto loginDto, HttpServletRequest request) {
        try {
            Authentication authentication = doAuthenticate(loginDto, request);
            UserDetails userDetails = ((UserDetails) authentication.getPrincipal());
            JwtResponse jwtResponse = generateAndCacheToken(userDetails);
            RefreshToken refreshToken = createRefreshToken(userDetails);
            jwtResponse.setRefreshToken(refreshToken.getToken());
            return jwtResponse;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(
                    messageUtil.getMessage("message.badCredentials"));
        }
    }

    public JwtResponse generateAndCacheToken(UserDetails userDetails) {
        JwtResponse jwtResponse = jwtHandler.generateJwtResponse(userDetails);
        Cache tokenCache = cacheService.getCache(SmsPanelRepository.TOKEN_CACHE);
        tokenCache.put(userDetails.getUsername(), jwtResponse);
        return jwtResponse;
    }

    private Authentication doAuthenticate(LoginDto loginDto, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());
        token.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        Authentication authentication = null;
        try {
            authentication = authenticationManager.authenticate(token);
        } catch (BadCredentialsException e) {
            throw new UserNotFoundException(messageUtil.getMessage("message.badCredentials"));
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);
        return authentication;
    }

    @Override
    public void invalidateToken(String username) {
        Cache tokenCache = cacheService.getCache(SmsPanelRepository.TOKEN_CACHE);
        tokenCache.evictIfPresent(username);
        jwtHandler.deleteByUserName(username);
    }

    @Override
    public boolean logout(Authentication authentication) {
        UserDetails userDetails = ((UserDetails) authentication.getPrincipal());
        invalidateToken(userDetails.getUsername());
        jwtHandler.deleteByUserName(userDetails.getUsername());
        return true;
    }

    @Override
    public void validateToken(LogoutDto logoutDto, HttpServletRequest request) {
        String token = request.getHeader(HttpHeaders.AUTHORIZATION);
        token = token.replace("Bearer ", "");
        if (!token.equals(logoutDto.token())) {
            Assert.notNull(token, messageUtil.getMessage("auth.message.invalidToken"));
        }
    }

    public RefreshToken createRefreshToken(UserDetails userDetails) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findByUsername(userDetails.getUsername()));
        refreshToken.setExpiryDate(LocalDateTime.now().plusSeconds(refreshTokenDuration));
        refreshToken.setToken(generateRefreshTokenFromUsername(userDetails.getUsername()));

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }

    private String generateRefreshTokenFromUsername(String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setIssuer("auth-server")
                .setSubject(username).setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + refreshTokenDuration * 1000))
                .signWith(key())
                .compact();
    }

    private Key key() {
        return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
    }

    public TokenRefreshResponse getTokenByRefreshToken(TokenRefreshRequest request) {
        String requestRefreshToken = request.getRefreshToken();
        return findByToken(requestRefreshToken)
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    JwtResponse jwtResponse = generateAndCacheToken(user);
                    return new TokenRefreshResponse(jwtResponse.getAccessToken(), requestRefreshToken);
                })
                .orElseThrow(() -> new TokenException(
                        String.valueOf(ResultResponseStatus.REFRESH_TOKEN_IS_NOT_IN_DATABASE.getStatus()),
                        messageUtil.getMessage(ResultResponseStatus.REFRESH_TOKEN_IS_NOT_IN_DATABASE.getDescription(), requestRefreshToken)
                ));
    }

    private RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo((LocalDateTime.now())) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenException(String.valueOf(
                    ResultResponseStatus.TOKEN_EXPIRED.getStatus()),
                    ResultResponseStatus.TOKEN_EXPIRED.getDescription());
        }
        return token;
    }

    private Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
}