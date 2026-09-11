package com.warehouse.mcp.domain.vo;

import java.util.Set;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public record CurrentUserProfile(UserId userId,
                                 String username,
                                 String firstName,
                                 String lastName,
                                 String role,
                                 String departmentCode,
                                 OperatorId operatorId) {

    private static final Set<String> MANAGING_ROLES = Set.of("MANAGER", "ADMIN", "SUPERADMIN");

    public boolean canManageDevices() {
        return role != null && MANAGING_ROLES.contains(role);
    }
}
