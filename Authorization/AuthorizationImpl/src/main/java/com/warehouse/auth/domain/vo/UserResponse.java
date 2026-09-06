package com.warehouse.auth.domain.vo;

import com.warehouse.auth.domain.model.User;
import com.warehouse.commonassets.identificator.DepartmentCode;

import lombok.Builder;

@Builder
public record UserResponse(String username, DepartmentCode departmentCode, boolean nonExpired, boolean enabled,
		boolean nonLocked) {

	public static UserResponse from(final User user, final DepartmentCode departmentCode) {
		return new UserResponse(user.getUsername(), departmentCode, false, user.isDeleted(),
				user.isInitial());
	}
}
