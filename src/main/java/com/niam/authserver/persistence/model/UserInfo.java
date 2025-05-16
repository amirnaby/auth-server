package com.niam.authserver.persistence.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserInfo {
    private String firstName;
    private String lastName;
    private String username;
    private boolean enabled;
    private Collection<? extends GrantedAuthority> authorities;
}