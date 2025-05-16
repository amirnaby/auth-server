package com.niam.authserver.persistence.model;

import jakarta.persistence.*;

import lombok.Data;

import java.util.Collection;

@SequenceGenerator(name = "acl_object_identity_sequence", sequenceName = "acl_object_identity_sequence", allocationSize = 1)
@Entity
@Data
@Table(name = "acl_object_identity")
public class Privilege {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_object_identity_sequence")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "owner_sid")
    private Role role;

    @ManyToOne
    @JoinColumn(name = "object_id_class")
    private AclClass aclClass;

    @Column(name = "object_id_identity")
    private String permissible;

    @ManyToOne
    @JoinColumn(name = "parent_object")
    private Privilege parentPrivilege;

//    @Column(columnDefinition="NUMBER(1)")
    private boolean entriesInheriting;

    @OneToMany(mappedBy = "aclObjectIdentity")
    private Collection<AclEntry> aclEntries;
}