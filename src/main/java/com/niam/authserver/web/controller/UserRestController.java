package com.niam.authserver.web.controller;

import com.niam.authserver.service.ActiveUserStore;
import com.niam.authserver.service.UserService;
import com.niam.commonservice.utils.ResponseEntityUtil;
import com.niam.commonservice.model.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class UserRestController {
    private final ActiveUserStore activeUserStore;
    private final UserService userService;
    private final ResponseEntityUtil responseEntityUtil;

    @GetMapping
    public ResponseEntity<ServiceResponse> getAllUsers(@RequestParam Map<String, String> searchParams) {
        return responseEntityUtil.ok(userService.getAllUsers(searchParams));
    }

    @GetMapping("/{username}")
    public ResponseEntity<ServiceResponse> getUserByUsername(@PathVariable String username) {
        return responseEntityUtil.ok(userService.findUserByUsername(username));
    }

    @PutMapping("/{username}")
    public ResponseEntity<ServiceResponse> updateRolesOfUser(@PathVariable String username, @RequestBody Map<String, Object> request) {
        userService.saveRegisteredUser(username, request);
        return responseEntityUtil.ok();
    }

    @GetMapping("/logged")
    public Set<String> getLoggedUsers() {
        return activeUserStore.getUsers();
    }

    @DeleteMapping("/delete/{username}")
    public ResponseEntity<ServiceResponse> deleteUser(@PathVariable String username) {
        userService.deleteUser(username);
        return responseEntityUtil.ok();
    }
}