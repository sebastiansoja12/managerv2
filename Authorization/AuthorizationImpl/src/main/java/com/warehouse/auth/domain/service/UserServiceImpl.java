package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.event.*;
import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.FullNameChangeCommand;
import com.warehouse.auth.domain.model.UpdateUserCommand;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.DepartmentServicePort;
import com.warehouse.auth.domain.port.secondary.UserRepository;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.auth.domain.vo.UserResponse;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final DepartmentServicePort departmentServicePort;

    public UserServiceImpl(final UserRepository userRepository, final DepartmentServicePort departmentServicePort) {
        this.userRepository = userRepository;
        this.departmentServicePort = departmentServicePort;
    }

    @Override
    public RegisterResponse create(final User user) {
        userRepository.createOrUpdate(user);
        final DepartmentCode departmentCode = departmentServicePort.getDepartmentCode(user.getDepartmentId());
        final UserResponse userResponse = UserResponse.from(user, departmentCode);
        DomainRegistry.eventPublisher().publishEvent(new UserCreatedEvent(user.snapshot()));
        return new RegisterResponse(userResponse);
    }

    @Override
    public User findUser(final String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public List<User> findAll() {
        return userRepository.findAll();
    }

    @Override
    public User update(final UpdateUserCommand command) {
        final User user = userRepository.findById(command.userId());
        if (user == null) {
            throw new AuthenticationErrorException("User does not exist");
        }
        final DepartmentId departmentId = departmentServicePort.getDepartmentId(command.departmentCode());
        user.update(command, departmentId);
        userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserChangedEvent(user.snapshot()));
        return user;
    }

    @Override
    public UserId nextUserId() {
        return new UserId((System.currentTimeMillis() % 900000) + 100000);
    }

    @Override
    public void changeFullName(final FullNameChangeCommand request) {
        final User user = this.userRepository.findById(request.getUserId());
        user.changeFullName(request);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserFullNameChangedEvent(user.snapshot()));
    }

    @Override
    public void changePassword(final UserId userId, final String encodedPassword) {
        final User user = this.userRepository.findById(userId);
        user.changePassword(encodedPassword);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserChangedEvent(user.snapshot()));
    }

    @Override
    public void changeLanguage(final UserId userId, final String language) {
        final User user = this.userRepository.findById(userId);
        user.changeLanguage(language);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserChangedEvent(user.snapshot()));
    }

    @Override
    public void changeApiKey(final UserId userId, final String apiKey) {
        final User user = this.userRepository.findById(userId);
        user.changeApiKey(apiKey);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserChangedEvent(user.snapshot()));
    }

    @Override
    public void changeRole(final UserId userId, final User.Role role) {
        final User user = this.userRepository.findById(userId);
        user.changeRole(role);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserRoleChangedEvent(user.snapshot()));
    }

    @Override
    public void addPermission(final UserId userId, final String permission) {
        final User user = this.userRepository.findById(userId);
        user.addPermission(permission);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserRoleAddedEvent(user.snapshot()));
    }

    @Override
    public void removePermission(final UserId userId, final String permission) {
        final User user = this.userRepository.findById(userId);
        user.removePermission(permission);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserRoleRemovedEvent(user.snapshot()));
    }

    @Override
    public User findUserById(final UserId userId) {
        return this.userRepository.findById(userId);
    }

    @Override
    public User findByEmail(final String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public List<UserId> findAllActiveUsersByDepartmentCode(final DepartmentCode departmentCode) {
        final DepartmentId departmentId = departmentServicePort.getDepartmentId(departmentCode);
        return this.userRepository.findAllActiveUsersByDepartmentId(departmentId);
    }

    @Override
    public void deleteDataForUser(final UserId userId) {
        final User user = this.userRepository.findById(userId);
        user.markAsDeleted();
        this.userRepository.createOrUpdate(user);
    }

    @Override
    public void updateDefaultDepartmentUser(final UserDepartmentUpdateRequest request) {
        final User user = this.userRepository.findById(request.userId());
        user.updateUserInfo(request);
        this.userRepository.createOrUpdate(user);
        DomainRegistry.eventPublisher().publishEvent(new UserChangedEvent(user.snapshot()));
    }

    @Override
    public UserId findInitialUser() {
        return this.userRepository.findInitialUser();
    }

    @Override
    public DepartmentCode getDepartmentCode(final DepartmentId departmentId) {
        return departmentServicePort.getDepartmentCode(departmentId);
    }
}
