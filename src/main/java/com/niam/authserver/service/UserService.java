package com.niam.authserver.service;

import com.niam.authserver.persistence.model.User;
import com.niam.authserver.web.dto.PasswordDto;
import com.niam.authserver.web.dto.UserDto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    User registerNewUserAccount(UserDto accountDto);

    void saveRegisteredUser(String username, Map<String, Object> request);

    void deleteUser(String username);

    List<User> getAllUsers(Map<String, String> searchParams);

    User findUserByUsername(String username);

    Optional<User> getUserByID(long id);

    Boolean changeUserPassword( PasswordDto passwordDto);

    String resetUserPassword(String userName);

    boolean checkIfValidOldPassword(User user, String password);
}
