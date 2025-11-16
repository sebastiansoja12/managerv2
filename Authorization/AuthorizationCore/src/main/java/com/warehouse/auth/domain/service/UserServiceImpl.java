package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.FullNameRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public RegisterResponse create(final User user) {
        final UserResponse userResponse = userRepository.createOrUpdate(user);
        return new RegisterResponse(userResponse);
    }

    @Override
    public User findUser(final String username) {
        return userRepository.findByUsername(username);
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
    public void removePermission(final UserId userId, final String permission) {
        final User user = this.userRepository.findById(userId);
        user.removePermission(permission);
        this.userRepository.createOrUpdate(user);
    }

    @Override
    public User findUserById(final UserId userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode) {
        return this.userRepository.findAllActiveUsersByDepartmentCode(departmentCode);
    }

    @Override
    public void deleteDataForUser(final UserId userId) {
        final User user = this.userRepository.findById(userId);
        user.markAsDeleted();
        this.userRepository.createOrUpdate(user);
    }
}