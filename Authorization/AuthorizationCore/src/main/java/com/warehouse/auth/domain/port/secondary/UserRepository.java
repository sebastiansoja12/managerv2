package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

public interface UserRepository {

    AuthenticationResponse login(AuthenticationResponse authentication);

    UserResponse signup(User user);

    void logout(String token);
}
