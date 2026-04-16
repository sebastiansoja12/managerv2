package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.AdminCreateRequest;
import com.warehouse.auth.domain.model.RefreshTokenRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.commonassets.identificator.UserId;

public interface AuthenticationPort {
    AuthenticationResponse login(final LoginRequest loginRequest);
    RegisterResponse signup(final RegisterRequest registerRequest);
    UserId createAdminUser(final AdminCreateRequest request);
    void logout(final RefreshTokenRequest refreshTokenRequest);
    void initiatePasswordReset(final String email);
}
