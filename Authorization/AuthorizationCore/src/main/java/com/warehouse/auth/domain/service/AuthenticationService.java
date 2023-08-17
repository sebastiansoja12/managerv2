package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.LoginResponse;
import com.warehouse.auth.domain.model.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.model.UserLogout;

public interface AuthenticationService {

    RegisterResponse register(User user);

    LoginResponse login(User user);

    User findUser(String username);

    void logout(UserLogout userLogout);
}
