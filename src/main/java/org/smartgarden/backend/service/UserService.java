package org.smartgarden.backend.service;

import org.smartgarden.backend.entity.User;

import java.util.List;

public interface UserService {
    User createUser(String username, String password, String role);
    List<User> listUsers();
}


