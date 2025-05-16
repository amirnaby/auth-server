package com.niam.authserver.persistence.model;

import com.niam.authserver.persistence.utils.PermissionConverter;
import jakarta.persistence.*;
import lombok.Data;

@SequenceGenerator(name = "acl_entry_sequence", sequenceName = "acl_entry_sequence", allocationSize = 1)
@Data
@Entity
@Table(name = "acl_entry")
public class AclEntry {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "acl_entry_sequence")
    private Long id;
    @ManyToOne
    @JoinColumn(name = "acl_object_identity")
    private Privilege aclObjectIdentity;
    @Column(name = "ace_order")
    private Integer aceOrder;
    @ManyToOne
    @JoinColumn(name = "sid")
    private Role role;
    @Convert(converter = PermissionConverter.class)
    private SimplePermission mask;
    //    @Column(columnDefinition="NUMBER(1)")
    private boolean granting;
    //    @Column(columnDefinition="NUMBER(1)")
    private boolean auditSuccess;
    //    @Column(columnDefinition="NUMBER(1)")
    private boolean auditFailure;
}