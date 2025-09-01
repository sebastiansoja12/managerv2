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
    public UserResponse saveUser(final User user) {
        final UserEntity userEntity = UserEntity.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .password(user.getPassword())
                .role(user.getRole())
                .departmentCode(user.getDepartmentCode())
                .apiKey(user.getApiKey())
                .build();

        repository.save(userEntity);

        return UserResponse.from(userEntity);
    }

    @Override
    public User findByUsername(final String username) {
        return repository.findByUsername(username).map(
                entity -> {
                    final User user = new User();
                    user.setUserId(entity.getUserId());
                    user.setUsername(entity.getUsername());
                    user.setEmail(entity.getEmail());
                    user.setFirstName(entity.getFirstName());
                    user.setLastName(entity.getLastName());
                    user.setPassword(entity.getPassword());
                    user.setRole(entity.getRole());
                    user.setDepartmentCode(entity.getDepartmentCode());
                    return user;
                }
        ).orElseThrow(() -> new UserNotFoundException("User was not found"));
    }

    @Override
    public User findByApiKey(final String apiKey) {
        return repository.findByApiKey(apiKey).map(entity -> {
            final User user = new User();
            user.setUserId(entity.getUserId());
            user.setUsername(entity.getUsername());
            user.setEmail(entity.getEmail());
            user.setFirstName(entity.getFirstName());
            user.setLastName(entity.getLastName());
            user.setPassword(entity.getPassword());
            user.setRole(entity.getRole());
            user.setDepartmentCode(entity.getDepartmentCode());
            user.setApiKey(entity.getApiKey());
            return user;
        }).orElseGet(() -> null);
    }
}
