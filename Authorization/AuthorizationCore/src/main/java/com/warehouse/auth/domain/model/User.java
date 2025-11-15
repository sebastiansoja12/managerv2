package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.event.UserChangedEvent;
import com.warehouse.auth.domain.event.UserCreatedEvent;
import com.warehouse.auth.domain.event.UserFullNameChangedEvent;
import com.warehouse.auth.domain.event.UserLoggedOutEvent;
import com.warehouse.auth.domain.exception.UserDeletedException;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.vo.UserSnapshot;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


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

    private Set<RolePermission> permissions;

    private Boolean deleted;

    private Instant createdAt;

    private Instant updatedAt;

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
        this.permissions = new HashSet<>();
    }

    public User(final UserId userId,
                final String username,
                final String password,
                final String firstName,
                final String lastName,
                final String email,
                final Role role,
                final DepartmentCode departmentCode,
                final String apiKey,
                final Set<RolePermission> permissions) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.role = role;
        this.departmentCode = departmentCode;
        this.apiKey = apiKey;
        this.permissions = permissions;
        this.deleted = false;
        this.createdAt = Instant.now();
        this.updatedAt = Instant.now();
        DomainRegistry.eventPublisher().publishEvent(new UserCreatedEvent(this.snapshot()));
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
    
    public static User createAdmin(
                            final UserId userId,
                            final String username,
                            final String password,
                            final String firstName,
                            final String lastName,
                            final String email,
                            final DepartmentCode departmentCode,
                            final String apiKey) {
        final Set<RolePermission> adminPermissions = DomainRegistry.rolePermissionService().findAllAdminPermissions();
        return new User(userId, username, password, firstName, lastName, email, Role.ADMIN, departmentCode, apiKey, adminPermissions);
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

    public void setPermissions(final Set<RolePermission> permissions) {
        this.permissions = permissions;
    }

    public void setCreatedAt(final Instant createdAt) {
        this.createdAt = createdAt;
    }

    public void setDeleted(final Boolean deleted) {
        this.deleted = deleted;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Set<RolePermission> getPermissions() {
        if (permissions == null) {
            this.permissions = new HashSet<>();
        }
        return permissions;
    }

    private UserSnapshot snapshot() {
        return new UserSnapshot(userId, username, password, email, role, departmentCode.getValue());
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {

        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        if (permissions != null) {
            authorities.addAll(
                    permissions.stream().map(p -> new SimpleGrantedAuthority(p.getPermission().name())).collect(Collectors.toSet()));
        }

        return authorities;
    }

    public void changeFullName(final FullNameRequest request) {
        this.firstName = request.getFirstName();
        this.lastName = request.getLastName();
        markAsModified();
        DomainRegistry.publish(new UserFullNameChangedEvent(this.snapshot()));
    }

    public void changeRole(final Role role) {
        if (deleted) {
            throw new UserDeletedException("User is already deleted");
        }

        this.role = role;
        updatePermissionsForRole(role);
        markAsModified();
        DomainRegistry.eventPublisher().publishEvent(new UserChangedEvent(this.snapshot()));
    }

    private void updatePermissionsForRole(final Role role) {
        getPermissions().clear();

        switch (role) {
            case ADMIN -> getPermissions().addAll(DomainRegistry.rolePermissionService().findAllAdminPermissions());
            case SUPPLIER -> getPermissions().addAll(DomainRegistry.rolePermissionService().findAllSupplierPermissions());
            case USER -> {}
            default -> throw new IllegalStateException("Unexpected role: " + role);
        }
    }

    public void addPermission(final String permission) {
        final RolePermission rolePermission = DomainRegistry.rolePermissionService().findByName(permission);
        getPermissions().add(rolePermission);
        markAsModified();
    }

    private void markAsDeleted() {
        this.deleted = true;
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public boolean isAdmin() {
        return role.equals(Role.ADMIN);
    }

    public void markAsLoggedOut() {
        DomainRegistry.eventPublisher().publishEvent(new UserLoggedOutEvent(this.snapshot()));
        markAsModified();
    }

    public enum Permission {

        ROLE_ADMIN_READ("admin:read"),
        ROLE_ADMIN_UPDATE("admin:update"),
        ROLE_ADMIN_CREATE("admin:create"),
        ROLE_ADMIN_DELETE("admin:delete"),

        ROLE_MANAGER_READ("management:read"),
        ROLE_MANAGER_UPDATE("management:update"),
        ROLE_MANAGER_CREATE("management:create"),
        ROLE_MANAGER_DELETE("management:delete"),

        ROLE_SUPPLIER_READ("supplier:read"),
        ROLE_SUPPLIER_UPDATE("supplier:update"),
        ROLE_SUPPLIER_CREATE("supplier:create"),
        ROLE_SUPPLIER_DELETE("supplier:delete");

        private final String permission;

        Permission(final String permission) {
            this.permission = permission;
        }

        public String getPermission() {
            return permission;
        }
    }

    public enum Role {
        USER,
        ADMIN,
        MANAGER,
        SUPPLIER;
    }
}
