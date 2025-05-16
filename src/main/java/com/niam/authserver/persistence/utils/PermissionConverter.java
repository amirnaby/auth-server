package com.niam.authserver.persistence.utils;

import com.niam.authserver.persistence.model.SimplePermission;
import jakarta.persistence.AttributeConverter;

public class PermissionConverter implements AttributeConverter<SimplePermission, Integer> {
    @Override
    public Integer convertToDatabaseColumn(SimplePermission permission) {
        return permission.getCode();
    }

    @Override
    public SimplePermission convertToEntityAttribute(Integer mask) {
        return SimplePermission.valueOf(mask);
    }
}