package com.niam.authserver.web.controller;

import com.niam.authserver.persistence.model.Role;
import com.niam.authserver.service.RoleService;
import com.niam.commonservice.utils.ResponseEntityUtil;
import com.niam.commonservice.model.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/roles")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class RoleRestController {
    private final RoleService roleService;
    private final ResponseEntityUtil responseEntityUtil;

    @GetMapping
    public ResponseEntity<ServiceResponse> getAllRoles() {
        return responseEntityUtil.ok(roleService.findAll());
    }

    @GetMapping("/{roleName}")
    public ResponseEntity<ServiceResponse> getRoleByName(@PathVariable String roleName) {
        return responseEntityUtil.ok(roleService.findByName(roleName));
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> saveRole(@RequestBody Role role) {
        return responseEntityUtil.ok(roleService.save(role));
    }
}