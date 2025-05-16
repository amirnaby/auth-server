package com.niam.authserver.service.impl;

import com.niam.authserver.persistence.dao.RoleRepository;
import com.niam.authserver.persistence.dao.UserRepository;
import com.niam.authserver.persistence.model.Role;
import com.niam.authserver.persistence.model.User;
import com.niam.authserver.service.UserService;
import com.niam.authserver.web.dto.PasswordDto;
import com.niam.authserver.web.dto.UserDto;
import com.niam.authserver.web.exception.ResultResponseStatus;
import com.niam.authserver.web.exception.UserAlreadyExistException;
import com.niam.authserver.web.exception.UserNotFoundException;
import com.niam.authserver.web.exception.UserPasswordException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EntityManager entityManager;

    @Override
    public User registerNewUserAccount(final UserDto accountDto) {
        if (usernameExists(accountDto.getUsername())) {
            throw new UserAlreadyExistException("There is an account with the username: " + accountDto.getUsername());
        }
        final User user = new User();

        user.setFirstName(accountDto.getFirstName());
        user.setLastName(accountDto.getLastName());
        user.setPassword(passwordEncoder.encode(accountDto.getPassword()));
        user.setUsername(accountDto.getUsername());
        user.setPhone(accountDto.getPhone());
        Collection<Role> roles = new ArrayList<>(accountDto.getRoles().stream().map(
                role -> roleRepository.findById(role.getId()).orElseThrow()).toList());
        roles.addAll(roleRepository.findAllByNameIn(List.of("ROLE_CHANGE_PASSWORD")));
        user.setRoles(roles);
        user.setEnabled(accountDto.getEnabled() == null ? Boolean.TRUE : accountDto.getEnabled());
        user.setCredentialsNonExpired(true);
        user.setAccountNonLocked(true);
        user.setAccountNonExpired(true);
        return userRepository.save(user);
    }

    @Override
    public void saveRegisteredUser(final String username, Map<String, Object> request) {
        User user = findUserByUsername(username);
        if (request.containsKey("enabled"))
            user.setEnabled(Boolean.parseBoolean(request.remove("enabled").toString()));
        if (request.containsKey("roles")) {
            List<Integer> roleIds = (List<Integer>) request.get("roles");
            List<Role> roles = new ArrayList<>();
            for (Integer i : roleIds) {
                roles.add(roleRepository.findById(Long.valueOf(i)).orElseThrow());
            }
            user.setRoles(roles);
        }
        userRepository.save(user);
    }

    @Override
    public void deleteUser(final String username) {
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
    }

    @Override
    public List<User> getAllUsers(Map<String, String> searchParams) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = criteriaBuilder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        List<Predicate> predicates = new ArrayList<>();
        for (Map.Entry<String, String> entry : searchParams.entrySet()) {
            String fieldName = entry.getKey();
            String fieldValue = entry.getValue();
            if (fieldValue != null && !fieldValue.isEmpty()) {
                if (fieldName.equals("enabled")) {
                    boolean enabled = Boolean.parseBoolean(fieldValue);
                    predicates.add(criteriaBuilder.equal(root.get(fieldName), enabled));
                } else {
                    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get(fieldName)), "%" + fieldValue.toLowerCase() + "%"));
                }
            }
        }
        query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public User findUserByUsername(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> getUserByID(final long id) {
        return userRepository.findById(id);
    }

    @Override
    public Boolean changeUserPassword(PasswordDto passwordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        User user = findUserByUsername(passwordDto.getUsername());
        if (user == null || !Objects.equals(user.getUsername(), currentUser.getUsername()))
            throw new UserNotFoundException();
        if (!checkIfValidOldPassword(user, passwordDto.getOldPassword()) || Objects.equals(passwordDto.getOldPassword(), passwordDto.getNewPassword())) {
            throw new UserPasswordException(String.valueOf(ResultResponseStatus.CHANGE_PASSWORD_EXCEPTION.getStatus()),
                    ResultResponseStatus.CHANGE_PASSWORD_EXCEPTION.getDescription());
        }
        user.setPassword(passwordEncoder.encode(passwordDto.getNewPassword()));
        user.setCredentialsNonExpired(true);
        user.getRoles().removeIf(role -> role.getAuthority().equals("ROLE_CHANGE_PASSWORD"));
        userRepository.save(user);
        return true;
    }

    @Override
    public String resetUserPassword(final String username) {
        final User user = findUserByUsername(username);

        if (user == null) {
            throw new UserNotFoundException();
        }
        String newPass = UUID.randomUUID().toString().substring(0, 8);
        user.setPassword(passwordEncoder.encode(newPass));
        Collection<Role> roles = user.getRoles();
        if (!roles.contains(roleRepository.findByName("ROLE_CHANGE_PASSWORD")))
            roles.add(roleRepository.findByName("ROLE_CHANGE_PASSWORD"));
        user.setRoles(roles);
        userRepository.save(user);
        return newPass;
    }

    @Override
    public boolean checkIfValidOldPassword(final User user, final String oldPassword) {
        return passwordEncoder.matches(oldPassword, user.getPassword());
    }

    private boolean usernameExists(final String username) {
        return userRepository.findByUsername(username) != null;
    }
}