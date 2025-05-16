package com.niam.authserver.service;

import com.niam.authserver.persistence.model.BusinessSystem;

import java.util.List;

public interface BusinessSystemService {
    List<BusinessSystem> findAll();
}
