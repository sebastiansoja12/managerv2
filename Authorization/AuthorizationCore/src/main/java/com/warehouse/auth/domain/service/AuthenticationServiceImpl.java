package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.vo.UserResponse;
import org.springframework.stereotype.Service;

import com.warehouse.auth.domain.model.RegisterResponse;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;

    @Override
    public RegisterResponse register(User user) {
        final UserResponse userResponse = userRepository.signup(user);
        return new RegisterResponse(userResponse);
    }
}
