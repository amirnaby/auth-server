package com.niam.authserver.persistence.dao;

import com.niam.authserver.persistence.model.BusinessSystem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BusinessSystemRepository extends JpaRepository<BusinessSystem, Long> {
}
