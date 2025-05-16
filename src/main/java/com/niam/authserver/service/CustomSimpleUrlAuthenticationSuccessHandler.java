package com.niam.authserver.service;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component("authenticationSuccessHandler")
@RequiredArgsConstructor
public class CustomSimpleUrlAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    private final ActiveUserStore activeUserStore;
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        handle(request, response, authentication);
        addToLoggedUsers(request, authentication);
        clearAuthenticationAttributes(request);
    }

    protected void addToLoggedUsers(HttpServletRequest request, Authentication authentication) {
        final HttpSession session = request.getSession(false);
        if (session != null) {
            session.setMaxInactiveInterval(30 * 60);
            UserDetails user = (UserDetails) authentication.getPrincipal();
            LoggedUser loggedUser = new LoggedUser(user.getUsername(), activeUserStore);
            session.setAttribute("user", loggedUser);
        }
    }
}
