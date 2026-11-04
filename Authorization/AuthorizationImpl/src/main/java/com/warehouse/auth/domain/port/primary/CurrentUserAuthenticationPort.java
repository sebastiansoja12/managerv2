package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;

public interface CurrentUserAuthenticationPort {

    UserId getCurrentUserId();

    OrganizationUser getCurrentUser();

    CurrentUserAuthentication getCurrentUserAuthentication();

    DepartmentCode getActingDepartmentCode(final OrganizationUser user,
                                           final Optional<DepartmentCode> requestedDepartmentCode);
}
