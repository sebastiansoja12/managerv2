package com.warehouse.department.domain.service;

import com.warehouse.commonassets.identificator.UserId;

public interface AuthenticationService {
    UserId currentUser();
}
