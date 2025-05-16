package com.niam.authserver.service;

import jakarta.servlet.http.HttpSessionBindingEvent;
import jakarta.servlet.http.HttpSessionBindingListener;

import java.io.Serializable;

public record LoggedUser(String username, ActiveUserStore activeUserStore)
        implements HttpSessionBindingListener, Serializable {

    @Override
    public void valueBound(HttpSessionBindingEvent event) {
        LoggedUser loggedUser = (LoggedUser) event.getValue();
        activeUserStore.addUser(loggedUser.username());
    }

    @Override
    public void valueUnbound(HttpSessionBindingEvent event) {
        LoggedUser loggedUser = (LoggedUser) event.getValue();
        activeUserStore.removeUser(loggedUser.username());
    }
}