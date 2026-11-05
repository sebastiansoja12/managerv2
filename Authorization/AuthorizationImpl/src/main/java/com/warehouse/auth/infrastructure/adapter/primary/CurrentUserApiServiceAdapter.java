package com.warehouse.auth.infrastructure.adapter.primary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.domain.port.primary.CurrentUserAuthenticationPort;
import com.warehouse.auth.domain.vo.CurrentUserAuthentication;
import com.warehouse.auth.infrastructure.adapter.primary.mapper.ResponseMapper;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

public class CurrentUserApiServiceAdapter implements CurrentUserApiService {

    private final CurrentUserAuthenticationPort currentUserAuthenticationPort;

    private final ResponseMapper responseMapper;

    private final ActingDepartmentHeader actingDepartmentHeader;

    public CurrentUserApiServiceAdapter(final CurrentUserAuthenticationPort currentUserAuthenticationPort,
                                        final ResponseMapper responseMapper,
                                        final ActingDepartmentHeader actingDepartmentHeader) {
        this.currentUserAuthenticationPort = currentUserAuthenticationPort;
        this.responseMapper = responseMapper;
        this.actingDepartmentHeader = actingDepartmentHeader;
    }

    @Override
    public UserId getCurrentUserId() {
        return currentUserAuthenticationPort.getCurrentUserId();
    }

    @Override
    public UserDto getCurrentUser() {
        final OrganizationUser user = currentUserAuthenticationPort.getCurrentUser();
        return responseMapper.map(user, actingDepartmentCode(user));
    }

    @Override
    public CurrentUserAuthenticationDto getCurrentUserAuthentication() {
        final CurrentUserAuthentication authentication = currentUserAuthenticationPort.getCurrentUserAuthentication();
        return new CurrentUserAuthenticationDto(
                authentication.jwtToken(),
                responseMapper.map(authentication.user(), actingDepartmentCode(authentication.user()))
        );
    }

    private DepartmentCode actingDepartmentCode(final OrganizationUser user) {
        return currentUserAuthenticationPort.getActingDepartmentCode(user,
                actingDepartmentHeader.requestedDepartmentCode());
    }

    @Override
    public boolean canAccessDepartment(final DepartmentId departmentId) {
        return currentUserAuthenticationPort.getCurrentUser().canAccessDepartment(departmentId);
    }
}
