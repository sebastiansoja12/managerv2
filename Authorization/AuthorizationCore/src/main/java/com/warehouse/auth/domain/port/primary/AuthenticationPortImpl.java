package com.warehouse.auth.domain.port.primary;

import java.util.Set;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.model.AdminCreateRequest;
import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.secondary.MailServicePort;
import com.warehouse.auth.domain.service.*;
import com.warehouse.auth.domain.vo.*;
import com.warehouse.auth.infrastructure.adapter.secondary.Logger;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;


public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;

    private final UserService userService;

    private final JwtService jwtService;

    private final Logger logger;

    private final PasswordEncoder passwordEncoder;

    private final DepartmentService departmentService;

    private final MailServicePort mailServicePort;

    public AuthenticationPortImpl(final AuthenticationService authenticationService,
                                  final UserService userService,
                                  final JwtService jwtService,
                                  final Logger logger,
                                  final PasswordEncoder passwordEncoder,
                                  final DepartmentService departmentService,
                                  final MailServicePort mailServicePort) {
        this.authenticationService = authenticationService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.logger = logger;
        this.passwordEncoder = passwordEncoder;
        this.departmentService = departmentService;
        this.mailServicePort = mailServicePort;
    }

    @Override
    public AuthenticationResponse login(final LoginRequest loginRequest) {
        final User user = userService.findUser(loginRequest.username());

        if (user == null) {
            throw new AuthenticationErrorException("Invalid username or password");
        } else if (!passwordEncoder.matches(loginRequest.password(), user.getPassword())) {
            throw new AuthenticationErrorException("Invalid username or password");
        }

        final String authenticationToken = jwtService.generateToken(user);

        final LoginResponse loginResponse = authenticationService.login(
                UsernamePasswordAuthentication.from(user));

        return new AuthenticationResponse(authenticationToken, loginResponse);
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

        validateDepartmentCode(departmentCode);

        final String apiKey = jwtService.generateToken(firstName, username, role, departmentCode);

        final User user = new User(userId, username, password, email, firstName, lastName, role, departmentCode, apiKey, Set.of());

        return userService.create(user);
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

        validateDepartmentCode(departmentCode);

        final User user = User.createAdmin(userId, username, password, email, firstName, lastName, departmentCode, null);

        this.userService.create(user);

        return userId;
    }

    @Override
    public void logout(final RefreshTokenRequest refreshTokenRequest) {

        final User user = userService.findUser(refreshTokenRequest.getUsername());

        this.authenticationService.logout(
                user.getUserId(), refreshTokenRequest.getRefreshToken()
        );

        SecurityContextHolder.clearContext();

        logLogoutInformation(refreshTokenRequest.getUsername());
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

    private void logLogoutInformation(final String username) {
        logger.info("User {} has been successfully logged out", username);
    }

    private void validateDepartmentCode(final DepartmentCode departmentCode) {
        if (!this.departmentService.existsByDepartmentCode(departmentCode)) {
            throw new AuthenticationErrorException("Department with code " + departmentCode + " does not exist");
        }
    }
}
