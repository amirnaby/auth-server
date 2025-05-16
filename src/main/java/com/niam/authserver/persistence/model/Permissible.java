package com.niam.authserver.persistence.model;

import jakarta.persistence.*;
import lombok.Data;

@SequenceGenerator(name = "permissible_sequence", sequenceName = "permissible_sequence", allocationSize = 1)
@Data
@Entity
@Table(name = "permissible")
@Inheritance(strategy=InheritanceType.JOINED)
@DiscriminatorColumn
public abstract class Permissible {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "permissible_sequence")
    private Long id;

    public abstract Class<?> getType();
}

