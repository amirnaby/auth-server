package com.niam.authserver.config;

import com.niam.authserver.persistence.dao.RoleRepository;
import com.niam.authserver.persistence.dao.UserRepository;
import com.niam.authserver.persistence.model.Role;
import com.niam.authserver.persistence.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

@Component
@RequiredArgsConstructor
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private boolean alreadySetup = false;

    @Override
    @Transactional
    public void onApplicationEvent(final ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        final Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        final Role userRole = createRoleIfNotFound("ROLE_USER");
        createRoleIfNotFound("ROLE_CHANGE_PASSWORD");
        createUserIfNotFound("admin", "admin", "admin", "admin", new ArrayList<>(Arrays.asList(adminRole, userRole)));
        alreadySetup = true;
    }

    @Transactional
    public Role createRoleIfNotFound(final String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role = roleRepository.save(role);
        return role;
    }

    @Transactional
    public User createUserIfNotFound(final String username, final String firstName, final String lastName, final String password, final Collection<Role> roles) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            user = new User();
            user.setFirstName(firstName);
            user.setLastName(lastName);
            user.setPassword(passwordEncoder.encode(password));
            user.setUsername(username);
            user.setEnabled(true);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);
            user.setRoles(roles);
        }
        return userRepository.save(user);
    }
}