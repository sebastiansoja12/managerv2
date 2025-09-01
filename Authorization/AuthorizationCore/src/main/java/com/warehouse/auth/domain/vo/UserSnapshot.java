package com.warehouse.auth.domain.vo;

import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.Role;
import com.warehouse.commonassets.identificator.UserId;

public record UserSnapshot(UserId userId, String username, String password, String email, Role role, String departmentCode) {
    @Override
    public String toString() {
        return "UserSnapshot{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", departmentCode='" + departmentCode + '\'' +
                '}';
    }
}
