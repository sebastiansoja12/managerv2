package com.warehouse.department.domain.service;

import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.commonassets.identificator.UserId;

public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public UserId currentUser() {
        return (UserId) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
