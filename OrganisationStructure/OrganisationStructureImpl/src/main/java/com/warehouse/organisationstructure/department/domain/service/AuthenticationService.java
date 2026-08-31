package com.warehouse.organisationstructure.department.domain.service;

import com.warehouse.commonassets.identificator.UserId;

public interface AuthenticationService {
    UserId currentUser();
}
