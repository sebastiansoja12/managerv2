package com.warehouse.auth.domain.port.primary;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.exception.UserAccessDeniedException;
import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.secondary.MailServicePort;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.domain.vo.*;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;


public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentService departmentService;

    private final MailServicePort mailServicePort;

    private final ApiKeyEncoder apiKeyEncoder;

    private final OperatorAccessPolicy operatorAccessPolicy;

    public AuthenticationPortImpl(final AuthenticationService authenticationService,
                                  final UserService userService,
                                  final JwtService jwtService,
                                  final PasswordEncoder passwordEncoder,
                                  final DepartmentService departmentService,
                                  final MailServicePort mailServicePort,
                                  final ApiKeyEncoder apiKeyEncoder,
                                  final OperatorAccessPolicy operatorAccessPolicy) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.departmentService = departmentService;
        this.mailServicePort = mailServicePort;
        this.apiKeyEncoder = apiKeyEncoder;
        this.operatorAccessPolicy = operatorAccessPolicy;
    }

    @Override
    public AuthenticationResponse login(final LoginRequest loginRequest) {
        final OrganizationUser user = userService.findUser(loginRequest.username());

        if (user == null || user.isDeleted()) {
            throw new AuthenticationErrorException("Invalid username or password");
        } else if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthenticationErrorException("Invalid username or password");
        } else if (!departmentService.existsByDepartmentId(user.getDepartmentId(), user.operatorId())) {
            throw new AuthenticationErrorException("Invalid username or password");
        } else if (!operatorAccessPolicy.allowsAccess(user.operatorId(), java.time.Instant.now(), false)) {
            throw new AuthenticationErrorException("Operator access is blocked");
        }

        final String accessToken = jwtService.generateToken(user);

        final LoginResponse loginResponse = authenticationService.login(
                UsernamePasswordAuthentication.from(user));

        return new AuthenticationResponse(accessToken, loginResponse.refreshToken().value());
    }

    @Override
    public AuthenticationResponse refresh(final String refreshToken) {
        final LoginResponse loginResponse = authenticationService.refresh(refreshToken);
        final OrganizationUser user = userService.findUser(loginResponse.username());
        if (!operatorAccessPolicy.allowsAccess(user.operatorId(), java.time.Instant.now(), false)) {
            authenticationService.logout(loginResponse.refreshToken().value());
            throw new AuthenticationErrorException("Operator access is blocked");
        }
        final String accessToken = jwtService.generateToken(user);
        return new AuthenticationResponse(accessToken, loginResponse.refreshToken().value());
    }

    @Override
    public RegisterResponse signup(final RegisterRequest request) {

        final UserId userId = this.userService.nextUserId();

        final String username = request.getUsername();

        final String password = this.passwordEncoder.encode(request.getPassword());

        final String email = request.getEmail();

        final String firstName = request.getFirstName();

        final String lastName = request.getLastName();

        final OrganizationUser.Role role = mapRole(request.getRole());

        if (OrganizationUser.Role.SUPERADMIN.equals(role)) {
            throw new AuthenticationErrorException("Superadmin cannot be registered through signup");
        }

        final DepartmentCode departmentCode = request.getDepartmentCode();

        final String language = request.getLanguage();

        validateDepartmentCode(departmentCode);
        final DepartmentId departmentId = departmentService.getDepartmentId(departmentCode);

        final OrganizationUser user = OrganizationUser.createWithRole(
                userId, username, password, firstName, lastName, email, role, departmentId,
                apiKeyEncoder.encode(userId, username).key(), language
        );

        return userService.create(user);
    }

    @Override
    public UserId createUser(final CreateUserCommand command) {
        final UserId userId = userService.nextUserId();
        final OrganizationUser.Role role = mapRole(command.role());
        final String password = passwordEncoder.encode(command.password());
        validateDepartmentCode(command.departmentCode());
        final DepartmentId departmentId = departmentService.getDepartmentId(command.departmentCode());
        final OrganizationUser currentUser = userService.findUserById(authenticationService.currentUserId());
        if (!currentUser.canAssignRole(role) || !currentUser.canAccessDepartment(departmentId)) {
            throw new UserAccessDeniedException("Current user cannot create user with role " + role
                    + " in department " + command.departmentCode().getValue());
        }
        final OrganizationUser user = OrganizationUser.createWithRole(
                userId,
                command.username(),
                password,
                command.firstName(),
                command.lastName(),
                command.email(),
                role,
                departmentId,
                apiKeyEncoder.encode(userId, command.username()).key(),
                command.language()
        );
        userService.create(user);
        return userId;
    }

    @Override
    public UserId createOperatorUser(final CreateOperatorUserCommand command) {
        final UserId userId = userService.nextUserId();
        final String password = passwordEncoder.encode(command.password());
        final OrganizationUser.Role role = mapRole(command.role());
        validateDepartmentCode(command.departmentCode());
        final DepartmentId departmentId = departmentService.getDepartmentId(command.departmentCode());
        final OrganizationUser user = OrganizationUser.createWithRole(
                userId,
                command.username(),
                password,
                command.firstName(),
                command.lastName(),
                command.email(),
                role,
                departmentId,
                apiKeyEncoder.encode(userId, command.username()).key(),
                command.language()
        );
        user.assignOperator(command.operatorId());
        userService.create(user);
        return userId;
    }

    @Override
    public UserId createSupplierUser(final CreateSupplierUserCommand command) {
        final UserId userId = userService.nextUserId();
        final String password = passwordEncoder.encode(RandomPasswordService.generatePassword(32, true, true, true));
        final OrganizationUser user = OrganizationUser.createWithRole(
                userId,
                command.username(),
                password,
                command.firstName(),
                command.lastName(),
                command.email(),
                OrganizationUser.Role.SUPPLIER,
                command.departmentId(),
                apiKeyEncoder.encode(userId, command.username()).key(),
                command.language()
        );
        userService.create(user);
        return userId;
    }

    @Override
    public UserId createAdminUser(final AdminCreateRequest request) {

        final UserId userId = this.userService.nextUserId();

        final String username = RandomUsernameService.generateUsername(6, 15, true);

        final String password = this.passwordEncoder.encode(RandomPasswordService.generatePassword(10, true, true, true));

        final String email = request.getEmail();

        final String firstName = "ADMIN";

        final String lastName = "ADMIN";

        final DepartmentCode departmentCode = request.getDepartmentCode();

        final String language = request.getLanguage();

        validateDepartmentCode(departmentCode);
        final DepartmentId departmentId = departmentService.getDepartmentId(departmentCode);

        final OrganizationUser user = OrganizationUser.createAdmin(userId, username, password, firstName, lastName, email, departmentId,
                language, apiKeyEncoder.encode(userId, username).key());

        this.userService.create(user);

        return userId;
    }

    @Override
    public void logout(final String refreshToken) {
        this.authenticationService.logout(refreshToken);

        SecurityContextHolder.clearContext();
    }

    @Override
    public void initiatePasswordReset(final String email) {
        final OrganizationUser user = this.userService.findByEmail(email);
        if (user == null) {
            throw new AuthenticationErrorException("User with email " + email + " does not exist");
        }

    }

    private OrganizationUser.Role mapRole(final String value) {
        final String role = value.toUpperCase();
        return switch (role) {
            case "SUPERADMIN" -> OrganizationUser.Role.SUPERADMIN;
            case "ADMIN" -> OrganizationUser.Role.ADMIN;
            case "MANAGER" -> OrganizationUser.Role.MANAGER;
            case "SUPPLIER" -> OrganizationUser.Role.SUPPLIER;
            default -> OrganizationUser.Role.USER;
        };
    }

    private void validateDepartmentCode(final DepartmentCode departmentCode) {
        if (!this.departmentService.existsByDepartmentCode(departmentCode)) {
            throw new AuthenticationErrorException("Department with code " + departmentCode + " does not exist");
        }
    }

}
