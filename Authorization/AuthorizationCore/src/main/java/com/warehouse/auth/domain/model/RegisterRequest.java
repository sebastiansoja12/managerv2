package com.warehouse.auth.domain.model;

import org.apache.commons.lang3.StringUtils;

import com.warehouse.auth.domain.exception.AuthenticationErrorException;
import com.warehouse.auth.infrastructure.adapter.primary.dto.RegisterRequestDto;
import com.warehouse.commonassets.identificator.DepartmentCode;

import lombok.NonNull;


public class RegisterRequest {

    private String email;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String role;

    private DepartmentCode departmentCode;

	public RegisterRequest(final DepartmentCode departmentCode, final String email, final String firstName,
			final String lastName, final String password, final String role, final String username) {
		this.departmentCode = departmentCode;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.role = role;
		this.username = username;
	}

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public @NonNull String getEmail() {
        return email;
    }

    public void setEmail(@NonNull final String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    public @NonNull String getPassword() {
        return password;
    }

    public void setPassword(@NonNull final String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(final String role) {
        this.role = role;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public static RegisterRequest from(final RegisterRequestDto req) {
		return new RegisterRequest(new DepartmentCode(req.departmentCode()), req.email(), req.firstName(),
				req.lastName(), req.password(), req.role(), req.username());
    }

    public String getUsername() {
        if (StringUtils.isEmpty(username)) {
            throw new AuthenticationErrorException("Username cannot be empty");
        }
        return username;
    }
}
