package com.warehouse.terminal.domain.model.command;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;

public class DevicePairRequest {
    private String externalSystemId;
    private DepartmentCode departmentCode;
    private UserId userId;

    public DevicePairRequest(final String externalSystemId,
                             final DepartmentCode departmentCode,
                             final UserId userId) {
        this.externalSystemId = externalSystemId;
        this.departmentCode = departmentCode;
        this.userId = userId;
    }

    public String getExternalSystemId() {
        return externalSystemId;
    }

    public void setExternalSystemId(final String externalSystemId) {
        this.externalSystemId = externalSystemId;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }
}
