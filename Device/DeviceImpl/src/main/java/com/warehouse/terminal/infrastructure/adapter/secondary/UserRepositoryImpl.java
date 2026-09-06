package com.warehouse.terminal.infrastructure.adapter.secondary;

import java.util.Objects;
import java.util.UUID;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.Username;
import com.warehouse.terminal.domain.port.secondary.UserRepository;
import com.warehouse.terminal.domain.port.secondary.UserServicePort;
import com.warehouse.terminal.domain.vo.User;
import com.warehouse.terminal.domain.vo.UserToken;

public class UserRepositoryImpl implements UserRepository {

    private final UserServicePort userServicePort;

    public UserRepositoryImpl(final UserServicePort userServicePort) {
        this.userServicePort = userServicePort;
    }

    @Override
    public User findByUsername(final Username username) {
        return this.userServicePort.findUserByUsername(username.value());
    }

    @Override
    public User findById(final UserId userId) {
        return this.userServicePort.findUserById(userId);
    }

    @Override
    public Boolean existsById(final UserId userId) {
        try {
            return this.userServicePort.findUserById(userId) != null;
        } catch (final RuntimeException exception) {
            return false;
        }
    }

    @Override
    public Boolean existsByIdAndDepartmentCode(final UserId userId, final DepartmentCode departmentCode) {
        try {
            final User user = this.userServicePort.findUserById(userId);
            return user != null && user.departmentCode() != null
                    && Objects.equals(user.departmentCode().value(), departmentCode.value());
        } catch (final RuntimeException exception) {
            return false;
        }
    }

    @Override
    public UserToken obtainUserToken(final UserId userId) {
        return new UserToken(UUID.randomUUID().toString());
    }

    @Override
    public Boolean existsByUsername(final Username username) {
        try {
            return this.userServicePort.findUserByUsername(username.value()) != null;
        } catch (final RuntimeException exception) {
            return false;
        }
    }
}
