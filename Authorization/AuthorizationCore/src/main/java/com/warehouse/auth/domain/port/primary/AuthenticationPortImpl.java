package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.service.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.auth.domain.vo.UserLogout;
import com.warehouse.auth.infrastructure.adapter.secondary.Logger;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.Role;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import lombok.extern.slf4j.Slf4j;


@Slf4j
public class AuthenticationPortImpl implements AuthenticationPort {

    private final AuthenticationService authenticationService;

    private final PasswordEncoder passwordEncoder;
    
    private final AuthenticationManager authenticationManager;

    private final JwtService jwtService;

    private final Logger logger;

    private final DepartmentService departmentService;

    public AuthenticationPortImpl(final AuthenticationService authenticationService,
                                  final PasswordEncoder passwordEncoder,
                                  final AuthenticationManager authenticationManager,
                                  final JwtService jwtService,
                                  final Logger logger,
                                  final DepartmentService departmentService) {
        this.authenticationService = authenticationService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.logger = logger;
        this.departmentService = departmentService;
    }

    @Override
    public AuthenticationResponse login(final LoginRequest loginRequest) {

        final User user = findUser(loginRequest.username());

        final String authenticationToken = jwtService.generateToken(user);

        authenticationService.login(user);

        return new AuthenticationResponse(authenticationToken);
    }

    @Override
    public RegisterResponse signup(final RegisterRequest request) {

        final UserId userId = this.authenticationService.nextUserId();

        final String username = request.getUsername();

        final String password = this.passwordEncoder.encode(request.getPassword());

        final String email = request.getEmail();

        final String firstName = request.getFirstName();

        final String lastName = request.getLastName();

        final Role role = mapRole(request.getRole());

        final DepartmentCode departmentCode = request.getDepartmentCode();

        validateDepartmentCode(departmentCode);

        final String apiKey = jwtService.generateToken(firstName, username, role, departmentCode);

        final User user = new User(userId, username, password, email, firstName, lastName, role, departmentCode, apiKey);

        return authenticationService.register(user);
    }

    @Override
    public User findUser(final String username) {
        return authenticationService.findUser(username);
    }

    @Override
    public void createAdminUser(final AdminCreateRequest request) {

        final UserId userId = this.authenticationService.nextUserId();

        final String username = RandomUsernameService.generateUsername(6, 15, true);

        final String password = this.passwordEncoder.encode(RandomPasswordService.generatePassword(10, true, true, true));

        final String email = request.getEmail();

        final String firstName = "ADMIN";

        final String lastName = "ADMIN";

        final Role role = Role.ADMIN;

        final DepartmentCode departmentCode = request.getDepartmentCode();

        validateDepartmentCode(departmentCode);

        final User user = new User(userId, username, password, firstName, lastName, email, role, departmentCode, null);

        authenticationService.register(user);
    }

    @Override
    public void updateFullName(final FullNameRequest request) {
        this.authenticationService.changeFullName(request);
    }

    @Override
    public void logout(final RefreshTokenRequest refreshTokenRequest) {

        final UserLogout userLogout = UserLogout.builder()
                .refreshToken(refreshTokenRequest.getRefreshToken())
                .username(refreshTokenRequest.getUsername())
                .build();

        authenticationService.logout(userLogout);

        logLogoutInformation(refreshTokenRequest.getUsername());
    }

    private Role mapRole(final String value) {
        final String role = value.toUpperCase();
        return switch (role) {
            case "ADMIN" -> Role.ADMIN;
            case "MANAGER" -> Role.MANAGER;
            case "SUPPLIER" -> Role.SUPPLIER;
            default -> Role.USER;
        };
    }

    private void logLogoutInformation(final String username) {
        logger.info("User {} has been successfully logged out", username);
    }

    private void validateDepartmentCode(final DepartmentCode departmentCode) {
        if (!departmentService.existsByDepartmentCode(departmentCode)) {
            throw new AuthenticationErrorException("Department with code " + departmentCode + " does not exist");
        }
    }
}
