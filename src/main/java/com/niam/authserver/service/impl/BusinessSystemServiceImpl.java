package com.niam.authserver.service.impl;

import com.niam.authserver.persistence.dao.BusinessSystemRepository;
import com.niam.authserver.persistence.model.BusinessSystem;
import com.niam.authserver.service.BusinessSystemService;
import lombok.AllArgsConstructor;
import org.springframework.security.access.prepost.PostFilter;
import org.springframework.stereotype.Service;

import java.util.List;
@AllArgsConstructor
@Service
public class BusinessSystemServiceImpl implements BusinessSystemService {
    private final BusinessSystemRepository businessSystemRepository;
    @Override
     @PostFilter("hasPermission(filterObject, 'READ')")
    public List<BusinessSystem> findAll() {

        return businessSystemRepository.findAll();
    }
}
