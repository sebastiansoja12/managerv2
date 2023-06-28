package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.*;

import java.util.List;

public interface AuthenticationPort {

    AuthenticationResponse login(LoginRequest loginRequest);

    RegisterResponse signup(RegisterRequest registerRequest);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
