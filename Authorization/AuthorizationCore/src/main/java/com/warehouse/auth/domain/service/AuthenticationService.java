package com.warehouse.auth.domain.service;

import org.springframework.security.core.Authentication;

import com.warehouse.auth.domain.model.LoginResponse;
import com.warehouse.auth.domain.model.RegisterResponse;
import com.warehouse.auth.domain.model.User;

public interface AuthenticationService {

    RegisterResponse register(User user);

    LoginResponse login(User user);

    User findUser(String username);
}
