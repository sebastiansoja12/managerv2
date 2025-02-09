package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Optional;
import java.util.UUID;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.port.secondary.UserRepository;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.domain.vo.UserToken;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.UserEntity;

public class UserRepositoryImpl implements UserRepository {

    private final UserReadRepository repository;

    public UserRepositoryImpl(final UserReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public User findByUsername(final Username username) {
        final Optional<UserEntity> user = this.repository.findByUsername(username);
        return user.map(User::from).orElseThrow();
    }

    @Override
    public User findById(final UserId userId) {
        final Optional<UserEntity> user = this.repository.findById(userId);
        return user.map(User::from).orElse(null);
    }

    @Override
    public Boolean existsById(final UserId userId) {
        return this.repository.existsById(userId);
    }

    @Override
    public UserToken obtainUserToken(final UserId userId) {
        return new UserToken(UUID.randomUUID().toString());
    }

    @Override
    public Boolean existsByUsername(final Username username) {
        return this.repository.existsByUsername(username);
    }
}
