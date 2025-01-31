package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.vo.LoginResponse;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.UserLogout;
import com.warehouse.commonassets.identificator.UserId;

public interface AuthenticationService {

    RegisterResponse register(User user);

    LoginResponse login(User user);

    User findUser(String username);

    void logout(UserLogout userLogout);

    UserId nextUserId();
}
