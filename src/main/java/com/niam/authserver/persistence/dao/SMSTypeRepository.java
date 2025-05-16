package com.niam.authserver.persistence.dao;

import com.niam.authserver.persistence.model.SMSType;
import com.niam.authserver.persistence.model.ServiceType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SMSTypeRepository extends JpaRepository<SMSType, Long> {
    List<SMSType> findSMSTypesByServiceType(ServiceType serviceType);
}