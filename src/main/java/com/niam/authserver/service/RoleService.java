package com.niam.authserver.service;

import com.niam.authserver.persistence.model.Role;

import java.util.List;

public interface RoleService {
    Role save(Role role);

    List<Role> findAll();

    Role findByName(String roleName);
}
