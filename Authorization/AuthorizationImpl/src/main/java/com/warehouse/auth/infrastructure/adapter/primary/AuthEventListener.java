package com.warehouse.auth.infrastructure.adapter.primary;

import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.warehouse.auth.event.OperatorCreatedEvent;
import com.warehouse.auth.domain.model.AdminCreateRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.AuthenticationPort;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.service.ApiKeyEncoder;
import com.warehouse.auth.domain.service.JwtService;
import com.warehouse.auth.domain.service.UserService;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.auth.infrastructure.adapter.primary.event.AdminUserCommand;
import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserChanged;
import com.warehouse.auth.infrastructure.adapter.primary.event.DepartmentUserDeleted;
import com.warehouse.auth.infrastructure.dto.RegisteringUserDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthEventListener {

    private final AuthenticationPort authenticationPort;

    private final UserPort userPort;

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final ApiKeyEncoder apiKeyEncoder;

    public AuthEventListener(final AuthenticationPort authenticationPort,
                             final UserPort userPort,
                             final UserService userService,
                             final JwtService jwtService,
                             final PasswordEncoder passwordEncoder,
                             final ApiKeyEncoder apiKeyEncoder) {
        this.authenticationPort = authenticationPort;
        this.userPort = userPort;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.apiKeyEncoder = apiKeyEncoder;
    }

    @EventListener
    public void handle(final AdminUserCommand command) {
        final AdminCreateRequest request = new AdminCreateRequest(command.getDepartmentCode(),
                command.getEmail(), command.getTelephoneNumber(), command.getLanguage().name());
        final UserId userId = this.authenticationPort.createAdminUser(request);
        command.getAdminCreatedId().accept(userId);
        log.info("Admin user created: {}", userId.getValue());
    }

    @EventListener
    public void handle(final OperatorCreatedEvent event) {
        final RegisteringUserDto registeringUser = event.getRegisteringUser();
        final DepartmentCode departmentCode = new DepartmentCode(registeringUser.departmentCode().value());
        final OperatorId operatorId = OperatorId.of(registeringUser.operatorId().value());
        final UserId userId = userService.nextUserId();
        final String password = passwordEncoder.encode(registeringUser.password());
        final String apiKey = apiKeyEncoder.encode(userId, registeringUser.username()).key();

        final User user = User.createAdmin(userId, registeringUser.username(), password, registeringUser.firstName(),
                registeringUser.lastName(), registeringUser.email(), departmentCode, registeringUser.language(), apiKey);
        user.assignOperator(operatorId);
        user.markAsInitial();

        userService.create(user);
        event.getUserCreatedId().accept(userId);
        log.info("Operator admin user created: {} for operator {}", userId.getValue(), operatorId.getValue());
    }

    @EventListener
    public void handle(final DepartmentUserDeleted event) {
        final DepartmentCode departmentCode = event.getDepartmentCode();
        this.userPort.deleteDataForDepartment(departmentCode);
        log.info("Department user deleted");
    }

    @EventListener
    public void handle(final DepartmentUserChanged event) {
        final DepartmentCode departmentCode = event.getDepartmentCode();
		final UserDepartmentUpdateRequest request = new UserDepartmentUpdateRequest(departmentCode, event.getUserId(),
				event.getTelephoneNumber(), event.getEmail());
        this.userPort.changeAdminDepartmentInfo(request);
        log.info("Department user updated");
    }
}
