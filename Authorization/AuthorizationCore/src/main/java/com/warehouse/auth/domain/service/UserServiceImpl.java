package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.RefreshToken;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.RefreshTokenRepository;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.*;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    
    private final RefreshTokenRepository refreshTokenRepository;

    private final RefreshTokenGenerator refreshTokenGenerator;

    public UserServiceImpl(final UserRepository userRepository,
                           final RefreshTokenRepository refreshTokenRepository,
                           final RefreshTokenGenerator refreshTokenGenerator) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.refreshTokenGenerator = refreshTokenGenerator;
    }

    @Override
    public RegisterResponse create(final User user) {
        final UserResponse userResponse = userRepository.createOrUpdate(user);
        return new RegisterResponse(userResponse);
    }

    @Override
    public LoginResponse login(final User user) {
		final RefreshToken refreshToken = RefreshToken.builder()
                .username(user.getUsername())
                .expired(false)
                .revoked(false)
                .token(refreshTokenGenerator.generateToken(user))
                .build();
		final Token token = refreshTokenRepository.save(refreshToken);
		return new LoginResponse(token);
    }

    @Override
    public User findUser(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public void logout(final UserLogout userLogout) {
        refreshTokenRepository.delete(userLogout.refreshToken());
    }

    @Override
    public UserId nextUserId() {
        return new UserId((System.currentTimeMillis() % 900000) + 100000);
    }

    @Override
    public void changeFullName(final FullNameRequest request) {
        final User user = this.userRepository.findByUsername(request.getUsername());
        user.changeFullName(request);
        this.userRepository.createOrUpdate(user);
    }

    @Override
    public void changeRole(final UserId userId, final User.Role role) {
        final User user = this.userRepository.findById(userId);
        user.changeRole(role);
        this.userRepository.createOrUpdate(user);
    }

    @Override
    public void addPermission(final UserId userId, final String permission) {
        final User user = this.userRepository.findById(userId);
        user.addPermission(permission);
        this.userRepository.createOrUpdate(user);
    }

    @Override
    public User findUserById(final UserId userId) {
        return this.userRepository.findById(userId);
    }
}