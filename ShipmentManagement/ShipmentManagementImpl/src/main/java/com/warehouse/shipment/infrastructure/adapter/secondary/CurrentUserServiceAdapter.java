package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.infrastructure.dto.CurrentUserAuthenticationDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.shipment.domain.port.secondary.CurrentUserServicePort;
import com.warehouse.shipment.domain.vo.UserContext;
import org.springframework.stereotype.Component;

@Component
public class CurrentUserServiceAdapter implements CurrentUserServicePort {

    private final CurrentUserApiService currentUserApiService;

    public CurrentUserServiceAdapter(final CurrentUserApiService currentUserApiService) {
        this.currentUserApiService = currentUserApiService;
    }

    @Override
    public UserId getCurrentUserId() {
        return currentUserApiService.getCurrentUserId();
    }

    @Override
    public UserContext getCurrentUserContext() {
        final CurrentUserAuthenticationDto auth = currentUserApiService.getCurrentUserAuthentication();
        return new UserContext(new UserId(auth.user().userId().value()), new DepartmentCode(auth.user().departmentCode()));
    }
}
