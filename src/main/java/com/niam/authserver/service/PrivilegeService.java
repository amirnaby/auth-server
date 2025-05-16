package com.niam.authserver.service;

import com.niam.authserver.persistence.model.Privilege;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface PrivilegeService {
    Privilege save(Privilege privilege);

    Page<Privilege> findAll(Privilege privilege, PageRequest pageRequest);
}
