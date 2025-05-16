package com.niam.authserver.web.controller;

import com.niam.authserver.service.UserService;
import com.niam.authserver.utils.ResponseEntityUtil;
import com.niam.authserver.web.dto.PasswordDto;
import com.niam.authserver.web.dto.UserDto;
import com.niam.authserver.web.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class RegistrationRestController {
    private final Logger LOGGER = LoggerFactory.getLogger(getClass());
    private final UserService userService;
    private final ResponseEntityUtil responseEntityUtil;

    @PostMapping(value = "/user/registration")
    public ResponseEntity<ServiceResponse> registerUserAccount(@RequestBody @Valid UserDto accountDto) {
        LOGGER.debug("Registering user account with information: {}", accountDto);
        return responseEntityUtil.ok(userService.registerNewUserAccount(accountDto));
    }

    @GetMapping("/user/resetPassword")
    public ResponseEntity<ServiceResponse> resetPassword(@RequestParam("username") final String username) {
        return responseEntityUtil.ok(userService.resetUserPassword(username));
    }

    @PostMapping("/user/updatePassword")
    @PreAuthorize("hasRole('ROLE_CHANGE_PASSWORD')")
    public ResponseEntity<ServiceResponse> changeUserPassword(@RequestBody @Valid PasswordDto passwordDto) {
        return responseEntityUtil.ok(userService.changeUserPassword(passwordDto));
    }
}