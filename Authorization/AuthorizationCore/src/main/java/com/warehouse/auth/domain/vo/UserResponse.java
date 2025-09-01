package com.warehouse.auth.domain.vo;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.identificator.DepartmentCode;

import lombok.Builder;

@Builder
public record UserResponse(String username, DepartmentCode departmentCode, boolean nonExpired, boolean enabled,
		boolean nonLocked) {

	public static UserResponse from(final UserEntity user) {
		return new UserResponse(user.getUsername(), user.getDepartmentCode(), user.isAccountNonExpired(),
				user.isEnabled(), user.isAccountNonLocked());
	}
}
