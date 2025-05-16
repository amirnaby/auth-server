package com.niam.authserver.persistence.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import jakarta.persistence.*;

@SequenceGenerator(name = "acl_sid_sequence", sequenceName = "acl_sid_sequence", allocationSize = 1)
@Entity
@Table(name = "acl_sid")
@Data
@NoArgsConstructor
public class Role implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "refresh_token_sequence")
    private Long id;

    @Column(name = "sid")
    private String name;

    private String title;

    @Column(name = "principal"/*, columnDefinition="NUMBER(1)"*/)
//    @Column(name = "principal")
    private boolean isRole;

    public Role(String name) {
        this.name = name;
    }

    @Override
    public String getAuthority() {
        return name;
    }
}