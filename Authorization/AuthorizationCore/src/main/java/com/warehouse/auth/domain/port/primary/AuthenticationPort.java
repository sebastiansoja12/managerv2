package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.*;

public interface AuthenticationPort {

    AuthenticationResponse login(LoginRequest loginRequest);

    RegisterResponse signup(RegisterRequest registerRequest);

    User findUser(String username);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
