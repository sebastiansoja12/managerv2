package com.warehouse.terminal.domain.service;

import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.port.secondary.UserRepository;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.domain.vo.UserToken;

public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User findByUsername(final Username username) {
        return this.userRepository.findByUsername(username);
    }

    @Override
    public UserToken validateUser(final UserId userId) {
        return userRepository.obtainUserToken(userId);
    }

    @Override
    public Boolean existsByUserId(final UserId userId) {
        return userRepository.existsById(userId);
    }

    @Override
    public Boolean existsByUsername(final Username username) {
        return userRepository.existsByUsername(username);
    }
}
