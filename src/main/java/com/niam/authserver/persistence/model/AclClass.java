package com.niam.authserver.persistence.model;

import jakarta.persistence.*;

@SequenceGenerator(name = "acl_class_sequence", sequenceName = "acl_class_sequence", allocationSize = 1)
@Entity
@Table(name = "acl_class")
public class AclClass {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_class_sequence")
    private Long id;
    @Column(name = "class")
    private String clazz;
}