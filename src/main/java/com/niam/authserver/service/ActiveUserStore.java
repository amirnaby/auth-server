package com.niam.authserver.service;

import java.util.Set;

public interface ActiveUserStore {
    Set<String> getUsers();

    void addUser(String username);

    void removeUser(String username);
}
