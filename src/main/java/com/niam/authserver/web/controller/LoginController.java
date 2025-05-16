package com.niam.authserver.web.controller;

import com.niam.authserver.persistence.model.User;
import com.niam.authserver.persistence.model.UserInfo;
import com.niam.authserver.service.CaptchaService;
import com.niam.authserver.service.LoginHandler;
import com.niam.authserver.service.LoginService;
import com.niam.authserver.utils.ResponseEntityUtil;
import com.niam.authserver.web.dto.LoginDto;
import com.niam.authserver.web.dto.LogoutDto;
import com.niam.authserver.web.request.TokenRefreshRequest;
import com.niam.authserver.web.response.ServiceResponse;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class LoginController {
    private final CaptchaService captchaService;
    private final LoginService loginService;
    private final LoginHandler loginHandler;
    private final ResponseEntityUtil responseEntityUtil;

    @PostMapping("/login")
    public ResponseEntity<ServiceResponse> login(final HttpServletRequest request, @RequestBody LoginDto loginDto) {
        return responseEntityUtil.ok(loginService.login(loginDto, request));
    }

    @PostMapping("/sign-out")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> logout(final HttpServletRequest request, @RequestBody LogoutDto logoutDto) {
        return responseEntityUtil.ok(loginService.logout(logoutDto, request));
    }

    @GetMapping("/captcha")
    public ResponseEntity<ServiceResponse> getCaptcha() {
        return responseEntityUtil.ok(captchaService.generateCaptcha());
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ServiceResponse> refreshToken(@Valid @RequestBody TokenRefreshRequest request) {
        return responseEntityUtil.ok(loginHandler.getTokenByRefreshToken(request));
    }

    @GetMapping("/user-info")
    public ResponseEntity<ServiceResponse> getUserInfo() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) authentication.getPrincipal();
        UserInfo userInfo = new UserInfo();
        userInfo.setFirstName(user.getFirstName());
        userInfo.setLastName(user.getLastName());
        userInfo.setUsername(user.getUsername());
        userInfo.setEnabled(user.isEnabled());
        userInfo.setAuthorities(user.getAuthorities());
        return responseEntityUtil.ok(userInfo);
    }
}