package com.niam.authserver.service.impl;

import com.niam.authserver.persistence.dao.RoleRepository;
import com.niam.authserver.persistence.model.Role;
import com.niam.authserver.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    @Override
    public Role findByName(String roleName) {
        return roleRepository.findByName(roleName);
    }
}