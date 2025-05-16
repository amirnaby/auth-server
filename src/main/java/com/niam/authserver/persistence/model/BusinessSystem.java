package com.niam.authserver.persistence.model;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
//@PrimaryKeyJoinColumn
@DiscriminatorValue("BusinessSystem")
public class BusinessSystem extends Permissible {
    private String name;
    private String description;

    @Override
    public Class<?> getType() {
        return BusinessSystem.class;
    }
}