package com.warehouse.auth.domain.port.secondary;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    AuthenticationResponse login(AuthenticationResponse authentication);

    void signup(UserEntity userEntity);

    void logout(String token);

    List<User> findByUsername(String username);

    Optional<UserEntity> findUserEntityByUsername(String username);
}
