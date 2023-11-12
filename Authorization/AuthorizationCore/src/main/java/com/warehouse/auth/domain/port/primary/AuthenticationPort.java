package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.vo.AuthenticationResponse;

public interface AuthenticationPort {

    AuthenticationResponse login(LoginRequest loginRequest);

    RegisterResponse signup(RegisterRequest registerRequest);

    User findUser(String username);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
