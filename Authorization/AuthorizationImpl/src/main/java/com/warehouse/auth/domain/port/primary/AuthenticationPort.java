package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.AdminCreateRequest;
import com.warehouse.auth.domain.model.CreateOperatorUserCommand;
import com.warehouse.auth.domain.model.CreateUserCommand;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.vo.AuthenticationResponse;
import com.warehouse.auth.domain.vo.LoginRequest;
import com.warehouse.auth.domain.vo.RegisterResponse;
import com.warehouse.commonassets.identificator.UserId;

public interface AuthenticationPort {
    AuthenticationResponse login(final LoginRequest loginRequest);
    AuthenticationResponse refresh(final String refreshToken);
    RegisterResponse signup(final RegisterRequest registerRequest);
    UserId createUser(final CreateUserCommand command);
    UserId createOperatorUser(final CreateOperatorUserCommand command);
    UserId createAdminUser(final AdminCreateRequest request);
    void logout(final String refreshToken);
    void initiatePasswordReset(final String email);
}
