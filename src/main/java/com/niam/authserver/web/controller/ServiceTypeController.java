package com.niam.authserver.web.controller;

import com.niam.authserver.persistence.dao.ServiceTypeRepository;
import com.niam.authserver.persistence.model.ServiceType;
import com.niam.authserver.utils.MessageUtil;
import com.niam.authserver.utils.ResponseEntityUtil;
import com.niam.authserver.web.response.ServiceResponse;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/service-type")
public class ServiceTypeController {
    private final ServiceTypeRepository serviceTypeRepository;
    private final MessageUtil messageSource;
    private final ResponseEntityUtil responseEntityUtil;

    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> saveServiceType(@RequestBody ServiceType serviceType) {
        return responseEntityUtil.ok(
//                serviceTypeRepository.save(serviceType)
        );
    }

    @DeleteMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ServiceResponse> deleteServiceType(@RequestBody ServiceType serviceType) {
        serviceTypeRepository.delete(serviceType);
        return responseEntityUtil.ok("ServiceType deleted");
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceResponse> findServiceType(@PathVariable Long id) {
        ServiceType serviceType = serviceTypeRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        messageSource.getMessage("exception.entity.not.found",
                                "ServiceType", id.toString())));
        return responseEntityUtil.ok(serviceType);
    }

    @GetMapping
    public ResponseEntity<ServiceResponse> findAllServiceTypes() {
        return responseEntityUtil.ok(serviceTypeRepository.findAll());
    }
}