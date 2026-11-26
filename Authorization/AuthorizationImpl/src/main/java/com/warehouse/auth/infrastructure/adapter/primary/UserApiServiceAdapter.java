package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.infrastructure.dto.UserApiKeyDto;
import com.warehouse.auth.UserApiService;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.model.CreateBusinessCustomerUserCommand;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.OperatorUserRequestMapper;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.CreateBusinessCustomerUserApiRequest;
import com.warehouse.auth.infrastructure.dto.CreateOperatorUserApiRequest;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import java.util.List;

public class UserApiServiceAdapter implements UserApiService {

    private final UserService userService;

    private final ResponseMapper responseMapper;

    private final AuthenticationPort authenticationPort;

    private final UserPort userPort;

    public UserApiServiceAdapter(final UserService userService,
                                 final ResponseMapper responseMapper,
                                 final AuthenticationPort authenticationPort,
                                 final UserPort userPort) {
        this.userService = userService;
        this.responseMapper = responseMapper;
        this.authenticationPort = authenticationPort;
        this.userPort = userPort;
    }

    @Override
    public UserDto findById(final UserId userId) {
        final OrganizationUser user = userService.findUserById(userId);
        return map(user);
    }

    @Override
    public UserDto findByUsername(final String username) {
        final OrganizationUser user = userService.findUser(username);
        return map(user);
    }

    @Override
    public UserIdDto findInitialUserForOperator() {
        final UserId userId = userService.findInitialUser();
        return new UserIdDto(userId.getValue());
    }

    @Override
    public List<UserDto> findAll() {
        return userService.findAll()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public UserIdDto createOperatorUser(final OperatorId operatorId, final CreateOperatorUserApiRequest request) {
        final UserId userId = authenticationPort.createOperatorUser(OperatorUserRequestMapper.toCommand(operatorId, request));
        return new UserIdDto(userId.value());
    }

    @Override
    public UserDto createBusinessCustomerUser(final CreateBusinessCustomerUserApiRequest request) {
        final UserId userId = authenticationPort.createBusinessCustomerUser(new CreateBusinessCustomerUserCommand(
                request.username(), request.name(), request.email(), request.language()));
        return map(userService.findUserById(userId));
    }

    @Override
    public String regenerateApiKey(final UserId userId) {
        return userPort.regenerateApiKey(userId);
    }

    @Override
    public List<UserApiKeyDto> findApiKeys() {
        return userService.findAll()
                .stream()
                .filter(OrganizationUser::hasApiKey)
                .filter(user -> !user.isBusinessCustomer())
                .filter(user -> !Boolean.TRUE.equals(user.isDeleted()))
                .map(user -> {
                    final com.warehouse.commonassets.identificator.DepartmentCode departmentCode =
                            user.getDepartmentId() != null ? userService.getDepartmentCode(user.getDepartmentId()) : null;
                    return new UserApiKeyDto(new UserIdDto(user.getUserId().value()), user.getUsername(),
                            user.getFirstName(), user.getLastName(), user.getRole().name(),
                            departmentCode != null ? departmentCode.getValue() : null);
                })
                .toList();
    }

    @Override
    public void revokeApiKey(final UserId userId) {
        final OrganizationUser user = userService.findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User " + userId.value() + " was not found");
        }
        userService.changeApiKey(userId, null);
    }

    @Override
    public int anonymizeOperatorUsers() {
        final List<OrganizationUser> users = userService.findAll();
        users.forEach(user -> userService.anonymize(user.getUserId()));
        return users.size();
    }

    private UserDto map(final OrganizationUser user) {
        if (user == null) {
            return null;
        }
        return responseMapper.map(user, userService.getDepartmentCode(user.getDepartmentId()));
    }
}
