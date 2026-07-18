package com.warehouse.auth.domain.port.primary;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.port.secondary.MailServicePort;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.domain.vo.*;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;


public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final JwtService jwtService;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentService departmentService;

    private final MailServicePort mailServicePort;

    private final ApiKeyEncoder apiKeyEncoder;

    public AuthenticationPortImpl(final AuthenticationService authenticationService,
                                  final UserService userService,
                                  final JwtService jwtService,
                                  final PasswordEncoder passwordEncoder,
                                  final DepartmentService departmentService,
                                  final MailServicePort mailServicePort,
                                  final ApiKeyEncoder apiKeyEncoder) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.departmentService = departmentService;
        this.mailServicePort = mailServicePort;
        this.apiKeyEncoder = apiKeyEncoder;
    }

    @Override
    public AuthenticationResponse login(final LoginRequest loginRequest) {
        final User user = userService.findUser(loginRequest.username());

        if (user == null) {
            throw new AuthenticationErrorException("Invalid username or password");
        } else if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthenticationErrorException("Invalid username or password");
        }

        final String accessToken = jwtService.generateToken(user);

        final LoginResponse loginResponse = authenticationService.login(
                UsernamePasswordAuthentication.from(user));

        return new AuthenticationResponse(accessToken, loginResponse.refreshToken().value());
    }

    @Override
    public AuthenticationResponse refresh(final String refreshToken) {
        final LoginResponse loginResponse = authenticationService.refresh(refreshToken);
        final User user = userService.findUser(loginResponse.username());
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

        final User.Role role = mapRole(request.getRole());

        final DepartmentCode departmentCode = request.getDepartmentCode();

        final String language = request.getLanguage();

        validateDepartmentCode(departmentCode);

        final User user = User.createWithRole(
                userId, username, password, firstName, lastName, email, role, departmentCode,
                apiKeyEncoder.encode(userId, username).key(), language
        );

        return userService.create(user);
    }

    @Override
    public UserId createUser(final CreateUserCommand command) {
        final UserId userId = userService.nextUserId();
        final User.Role role = mapRole(command.role());
        final String password = passwordEncoder.encode(command.password());
        validateDepartmentCode(command.departmentCode());
        final User user = User.createWithRole(
                userId,
                command.username(),
                password,
                command.firstName(),
                command.lastName(),
                command.email(),
                role,
                command.departmentCode(),
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
        final User.Role role = mapRole(command.role());
        final User user = User.createWithRole(
                userId,
                command.username(),
                password,
                command.firstName(),
                command.lastName(),
                command.email(),
                role,
                command.departmentCode(),
                apiKeyEncoder.encode(userId, command.username()).key(),
                command.language()
        );
        user.assignOperator(command.operatorId());
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

        final User user = User.createAdmin(userId, username, password, email, firstName, lastName, departmentCode,
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
        final User user = this.userService.findByEmail(email);
        if (user == null) {
            throw new AuthenticationErrorException("User with email " + email + " does not exist");
        }

    }

    private User.Role mapRole(final String value) {
        final String role = value.toUpperCase();
        return switch (role) {
            case "ADMIN" -> User.Role.ADMIN;
            case "MANAGER" -> User.Role.MANAGER;
            case "SUPPLIER" -> User.Role.SUPPLIER;
            default -> User.Role.USER;
        };
    }

    private void validateDepartmentCode(final DepartmentCode departmentCode) {
        if (!this.departmentService.existsByDepartmentCode(departmentCode)) {
            throw new AuthenticationErrorException("Department with code " + departmentCode + " does not exist");
        }
    }

}
