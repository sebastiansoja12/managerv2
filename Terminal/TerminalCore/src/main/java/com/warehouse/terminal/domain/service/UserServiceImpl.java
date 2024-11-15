package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.port.secondary.UserRepository;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.domain.vo.UserToken;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(final String username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public UserToken validateUser(final UserId userId) {
        return new UserToken("token");
    }

    @Override
    public Boolean existsByUserId(final UserId userId) {
        return userRepository.existsById(userId);
    }
}
