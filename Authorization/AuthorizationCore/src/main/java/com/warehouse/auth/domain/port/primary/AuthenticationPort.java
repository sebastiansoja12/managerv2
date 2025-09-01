package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;

public interface AuthenticationPort {

    AuthenticationResponse login(LoginRequest loginRequest);

    RegisterResponse signup(RegisterRequest registerRequest);

    User findUser(String username);

    void updateFullName(final FullNameRequest request);

    void logout(RefreshTokenRequest refreshTokenRequest);
}
