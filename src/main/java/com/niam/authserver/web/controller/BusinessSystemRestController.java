package com.niam.authserver.web.controller;

import com.niam.authserver.persistence.dao.BusinessSystemRepository;
import com.niam.authserver.persistence.model.BusinessSystem;
import com.niam.authserver.service.BusinessSystemService;
import com.niam.commonservice.model.response.ServiceResponse;
import com.niam.commonservice.utils.ResponseEntityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/business-system")
@RequiredArgsConstructor
public class BusinessSystemRestController {
    private final ResponseEntityUtil responseEntityUtil;
    private final BusinessSystemRepository businessSystemRepository;
    private final BusinessSystemService businessSystemService;

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> getById(@PathVariable Long id) {
        return responseEntityUtil.ok(businessSystemRepository.findById(id).orElse(null));
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> saveSystem(@RequestBody BusinessSystem system) {
        return responseEntityUtil.ok(businessSystemRepository.save(system));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> getAll() {
        return responseEntityUtil.ok(businessSystemService.findAll());
    }
}