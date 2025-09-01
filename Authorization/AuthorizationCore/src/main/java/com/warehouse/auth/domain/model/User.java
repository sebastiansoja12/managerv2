package com.warehouse.auth.domain.model;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.warehouse.auth.domain.event.UserCreatedEvent;
import com.warehouse.auth.domain.event.UserFullNameChangedEvent;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.vo.UserSnapshot;
import com.warehouse.auth.infrastructure.adapter.secondary.enumeration.Role;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;



public class User {

    private UserId userId;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private Role role;

    private DepartmentCode departmentCode;

    private String apiKey;

    public User() {

    }

    public User(final UserId userId,
                final String username,
                final String password,
                final String firstName,
                final String lastName,
                final String email,
                final Role role,
                final DepartmentCode departmentCode,
                final String apiKey) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.departmentCode = departmentCode;
        this.apiKey = apiKey;
        DomainRegistry.publish(new UserCreatedEvent(this.snapshot()));
    }

    public User(final UserId userId,
                final String username,
                final String firstName,
                final String lastName,
                final String email,
                final Role role,
                final DepartmentCode departmentCode) {
        this.userId = userId;
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.departmentCode = departmentCode;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public void setDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
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

    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(final Role role) {
        this.role = role;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(final String apiKey) {
        this.apiKey = apiKey;
    }

    private UserSnapshot snapshot() {
        return new UserSnapshot(userId, username, password, email, role, departmentCode.getValue());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return role.getAuthorities();
    }

    public void changeFullName(final FullNameRequest request) {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        markAsModified();
        DomainRegistry.publish(new UserFullNameChangedEvent(this.snapshot()));
    }

    private void markAsModified() {
    }
}
