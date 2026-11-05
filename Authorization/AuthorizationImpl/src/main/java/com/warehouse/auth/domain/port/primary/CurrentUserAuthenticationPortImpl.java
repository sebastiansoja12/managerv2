package com.warehouse.auth.domain.port.primary;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.service.CurrentUserAuthenticationService;
import com.warehouse.auth.domain.service.DepartmentService;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

import java.util.Optional;

public class CurrentUserAuthenticationPortImpl implements CurrentUserAuthenticationPort {

    private final CurrentUserAuthenticationService currentUserAuthenticationService;

    private final DepartmentService departmentService;

    public CurrentUserAuthenticationPortImpl(final CurrentUserAuthenticationService currentUserAuthenticationService,
                                             final DepartmentService departmentService) {
        this.currentUserAuthenticationService = currentUserAuthenticationService;
        this.departmentService = departmentService;
    }

    @Override
    public UserId getCurrentUserId() {
        return currentUserAuthenticationService.getCurrentUserId();
    }

    @Override
    public OrganizationUser getCurrentUser() {
        return currentUserAuthenticationService.getCurrentUser();
    }

    @Override
    public CurrentUserAuthentication getCurrentUserAuthentication() {
        return currentUserAuthenticationService.getCurrentUserAuthentication();
    }

    @Override
    public DepartmentCode getActingDepartmentCode(final OrganizationUser user,
                                                  final Optional<DepartmentCode> requestedDepartmentCode) {
        return departmentService.findActingDepartmentId(user, requestedDepartmentCode)
                .or(() -> Optional.ofNullable(user.getDepartmentId()))
                .map(departmentService::getDepartmentCode)
                .orElse(null);
    }
}
