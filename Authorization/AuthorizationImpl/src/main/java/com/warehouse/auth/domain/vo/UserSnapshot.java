package com.warehouse.auth.domain.vo;

import com.warehouse.auth.domain.model.User;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;

public record UserSnapshot(UserId userId, String username, String password, String email, User.Role role,
                           DepartmentId departmentId) {
    @Override
    public String toString() {
        return "UserSnapshot{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", role=" + role +
                ", departmentId=" + departmentId +
                '}';
    }
}
