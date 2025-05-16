package com.niam.authserver.web.controller;

import com.niam.authserver.persistence.model.Privilege;
import com.niam.authserver.service.PrivilegeService;
import com.niam.authserver.utils.ResponseEntityUtil;
import com.niam.authserver.web.response.ServiceResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/privileges")
@PreAuthorize("hasRole('ROLE_ADMIN')")
@RequiredArgsConstructor
public class PrivilegeRestController {
    
    private final PrivilegeService privilegeService;

    private final ResponseEntityUtil responseEntityUtil;

    @PutMapping("/{id}")
    public ResponseEntity<ServiceResponse> updatePrivilege(@RequestBody Privilege privilege) {
        return responseEntityUtil.ok(privilegeService.save(privilege));
    }

    @PostMapping
    public ResponseEntity<ServiceResponse> savePrivilege(@RequestBody Privilege privilege) {
        return responseEntityUtil.ok(privilegeService.save(privilege));
    }

    @GetMapping
    public ResponseEntity<ServiceResponse> getAllPrivileges(@RequestBody Privilege privilege,
                                                            @RequestParam(required = false) Integer page,
                                                            @RequestParam(required = false) Integer size) {
        page = (page == null? 0 : page);
        size = (size == null? 10 : size);
        return responseEntityUtil.ok(privilegeService.findAll(privilege, PageRequest.of(page, size)));
    }
}
