package com.niam.authserver.filter;

import com.niam.authserver.service.JwtHandler;
import com.niam.authserver.web.exception.InvalidTokenException;
import com.niam.authserver.web.exception.ResultResponseStatus;
import com.niam.authserver.web.exception.TokenException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.nio.file.AccessDeniedException;

public class JwtAuthFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtHandler jwtHandler;
    private final HandlerExceptionResolver handlerExceptionResolver;

    public JwtAuthFilter(UserDetailsService userDetailsService, JwtHandler jwtHandler, HandlerExceptionResolver handlerExceptionResolver) {
        this.userDetailsService = userDetailsService;
        this.jwtHandler = jwtHandler;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            String jwt = parseJwt(request);
            if (jwt != null) {
                String username = jwtHandler.validateJwtAndGetUsername(jwt);
                jwtHandler.isAlreadyLoggedInWithJwt(username, jwt);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(userDetails,
                                null,
                                userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            filterChain.doFilter(request, response);
        } catch (AccessDeniedException ade) {
            handlerExceptionResolver.resolveException(request, response, null, ade);
        } catch (SignatureException ade) {
            logger.error(ade);
            TokenException tokenException = new TokenException(String.valueOf(
                    ResultResponseStatus.TOKEN_SIGNATURE_EXCEPTION.getStatus()),
                    ResultResponseStatus.TOKEN_SIGNATURE_EXCEPTION.getDescription());
            handlerExceptionResolver.resolveException(request, response, null, tokenException);
        } catch (InvalidTokenException ade) {
            logger.error(ade);
            TokenException tokenException = new TokenException(String.valueOf(
                    ResultResponseStatus.INVALID_TOKEN_EXCEPTION.getStatus()),
                    ResultResponseStatus.INVALID_TOKEN_EXCEPTION.getDescription());
            handlerExceptionResolver.resolveException(request, response, null, tokenException);
        } catch (ExpiredJwtException ade) {
            logger.error(ade);
            TokenException tokenException = new TokenException(String.valueOf(
                    ResultResponseStatus.TOKEN_EXPIRED.getStatus()),
                    ResultResponseStatus.TOKEN_EXPIRED.getDescription());
            handlerExceptionResolver.resolveException(request, response, null, tokenException);
        }
    }

    private String parseJwt(HttpServletRequest request) {
        return request.getHeader(HttpHeaders.AUTHORIZATION);
    }
}