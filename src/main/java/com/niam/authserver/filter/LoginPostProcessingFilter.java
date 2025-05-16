package com.niam.authserver.filter;

import com.niam.authserver.web.exception.ResultResponseStatus;
import com.niam.authserver.web.exception.UserMustChangePasswordException;
import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

@AllArgsConstructor
public class LoginPostProcessingFilter implements Filter {
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        HttpServletResponse response = ((HttpServletResponse) servletResponse);
        HttpServletRequest request = ((HttpServletRequest) servletRequest);
        if (authentication != null && mustChangePassword(authentication)
                && !request.getRequestURI().contains("/user/resetPassword")
                && !request.getRequestURI().contains("/user/updatePassword")
                && !request.getRequestURI().contains("/user-info")) {
            UserMustChangePasswordException userPasswordException = new UserMustChangePasswordException(String.valueOf(ResultResponseStatus.USER_MUST_CHANGE_PASSWORD.getStatus()),
                    ResultResponseStatus.USER_MUST_CHANGE_PASSWORD.getDescription());
//            handlerExceptionResolver.resolveException(request, response, null, userPasswordException);
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    private boolean mustChangePassword(Authentication authentication) {
        for (GrantedAuthority authority : authentication.getAuthorities()) {
            if (authority.getAuthority().equals("ROLE_CHANGE_PASSWORD")) {
                return true;
            }
        }
        return false;
    }
}