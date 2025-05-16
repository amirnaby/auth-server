package com.niam.authserver.service.impl;

import com.niam.authserver.persistence.dao.PrivilegeRepository;
import com.niam.authserver.persistence.model.Privilege;
import com.niam.authserver.service.PrivilegeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PrivilegeServiceImpl implements PrivilegeService {
    private final PrivilegeRepository privilegeRepository;

    @Override
    public Privilege save(Privilege privilege) {
        return privilegeRepository.save(privilege);
    }

    @Override
    public Page<Privilege> findAll(Privilege privilege, PageRequest pageRequest) {
        return privilegeRepository.findAll(pageRequest);
    }
}