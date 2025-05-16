package com.niam.authserver.persistence.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "pnl_um_user")
@Data
//@PrimaryKeyJoinColumn
@DiscriminatorValue("User")
public class User extends Permissible implements UserDetails {
    private String firstName;
    private String lastName;
    private String username;
    private String phone;
    @Column(length = 60)
    private String password;
    //    @Column(columnDefinition = "NUMBER(1)")
    private boolean enabled;
    //    @Column(columnDefinition = "NUMBER(1)")
    private boolean accountNonLocked;
    //    @Column(columnDefinition = "NUMBER(1)")
    private boolean accountNonExpired;
    //    @Column(columnDefinition = "NUMBER(1)")
    private boolean credentialsNonExpired;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "pnl_um_users_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Collection<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    @Override
    public Class<?> getType() {
        return User.class;
    }
}