package com.niam.authserver.service.impl;

import com.niam.authserver.service.ActiveUserStore;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class ActiveUserStoreImpl implements ActiveUserStore {
    private final Set<String> users = new HashSet<>();

    @Override
    public Set<String> getUsers() {
        return Collections.unmodifiableSet(users);
    }

    @Override
    public void addUser(String username) {
        users.add(username);
    }

    @Override
    public void removeUser(String username) {
        users.remove(username);
    }
}