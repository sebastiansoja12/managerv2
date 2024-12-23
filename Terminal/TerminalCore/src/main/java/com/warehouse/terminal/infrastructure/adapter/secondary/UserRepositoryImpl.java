package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.port.secondary.UserRepository;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.UserEntity;

public class UserRepositoryImpl implements UserRepository {

    private final UserReadRepository repository;

    public UserRepositoryImpl(final UserReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findByUsername(final String username) {
        final Optional<UserEntity> user = this.repository.findByUsername(username);
        return user.map(User::from).orElseThrow();
    }

    @Override
    public User findById(final UserId userId) {
        final Optional<UserEntity> user = this.repository.findById(userId.getValue());
        return user.map(User::from).orElseThrow();
    }

    @Override
    public Boolean existsById(final UserId userId) {
        return this.repository.existsById(userId.getValue());
    }
}
