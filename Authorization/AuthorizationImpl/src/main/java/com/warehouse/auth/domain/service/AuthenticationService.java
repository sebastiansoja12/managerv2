package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.vo.LoginResponse;
import com.warehouse.auth.domain.vo.UsernamePasswordAuthentication;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public interface AuthenticationService {
    LoginResponse login(final UsernamePasswordAuthentication usernamePasswordAuthentication);
    LoginResponse refresh(final String refreshToken);
    void logout(final String refreshToken);
    UserId currentUserId();
    OperatorId currentOperatorId();
}
