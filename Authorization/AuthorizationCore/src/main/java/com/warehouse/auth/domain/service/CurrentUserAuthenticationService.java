package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.port.primary.UserPort;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.commonassets.identificator.UserId;

public class CurrentUserAuthenticationService {

    private final AuthenticationService authenticationService;

    private final UserPort userPort;

    private final JwtService jwtService;

    public CurrentUserAuthenticationService(final AuthenticationService authenticationService,
                                            final UserPort userPort,
                                            final JwtService jwtService) {
        this.authenticationService = authenticationService;
        this.userPort = userPort;
        this.jwtService = jwtService;
    }

    public CurrentUserAuthentication getCurrentUserAuthentication() {
        final User user = getCurrentUser();
        final String jwtToken = jwtService.generateToken(user);
        return new CurrentUserAuthentication(jwtToken, user);
    }

    public UserId getCurrentUserId() {
        return authenticationService.currentUserId();
    }

    public User getCurrentUser() {
        final UserId userId = getCurrentUserId();
        return userPort.findUser(userId);
    }
}
