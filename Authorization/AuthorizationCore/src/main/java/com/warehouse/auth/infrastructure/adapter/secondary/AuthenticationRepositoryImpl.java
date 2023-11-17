package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.auth.infrastructure.adapter.secondary.exception.UserNotFoundException;
import com.warehouse.auth.infrastructure.adapter.secondary.mapper.UserMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class AuthenticationRepositoryImpl implements UserRepository {

    private final AuthenticationReadRepository repository;

    private final UserMapper userMapper;

    @Override
    public UserResponse saveUser(User user) {
        final UserEntity userEntity = userMapper.map(user);

        repository.save(userEntity);

        return userMapper.mapToUserResponse(userEntity);
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username).map(userMapper::map).orElseThrow(
                () -> new UserNotFoundException("User was not found"));
    }
}
