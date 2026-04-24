package com.warehouse.terminal.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.port.secondary.UserServicePort;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.infrastructure.adapter.secondary.entity.UserEntity;

import java.util.Optional;

public class UserServiceAdapter implements UserServicePort {

    private final UserReadRepository userReadRepository;

    public UserServiceAdapter(final UserReadRepository userReadRepository) {
        this.userReadRepository = userReadRepository;
    }

    @Override
    public User findUserById(final UserId userId) {
        final Optional<UserEntity> userEntity = this.userReadRepository.findById(userId);
        return userEntity.map(User::from).orElse(null);
    }
}
