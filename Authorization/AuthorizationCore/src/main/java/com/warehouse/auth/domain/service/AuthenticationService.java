package com.warehouse.auth.domain.service;

import com.warehouse.auth.domain.model.*;
import com.warehouse.auth.domain.vo.AuthenticationToken;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AuthenticationService {

    RegisterResponse register(User user);
}
