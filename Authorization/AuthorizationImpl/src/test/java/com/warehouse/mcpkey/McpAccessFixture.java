package com.warehouse.mcpkey;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

public final class McpAccessFixture {

    public static final UserId ADMIN_ID = new UserId(10L);

    private McpAccessFixture() {
    }

    public static OrganizationUser user(final UserId userId, final OrganizationUser.Role role,
                                        final OperatorId operatorId, final DepartmentId departmentId) {
        final OrganizationUser user = OrganizationUser.of(userId, "user-" + userId.value(), "password", "Jan", "Nowak",
                "user@example.com", role, departmentId, null);
        user.assignOperator(operatorId);
        return user;
    }
}
