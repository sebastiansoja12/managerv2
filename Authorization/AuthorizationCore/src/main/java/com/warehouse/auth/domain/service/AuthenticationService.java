package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.AuthenticationResponse;
import com.warehouse.auth.domain.model.LoginRequest;
import com.warehouse.auth.domain.model.RegisterRequest;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.AuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthenticationService {

    AuthenticationResponse createToken(AuthenticationToken authenticationToken);
}
