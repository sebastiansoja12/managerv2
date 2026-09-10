package com.warehouse.auth.domain.model;

import com.warehouse.auth.domain.event.UserChangedEvent;
import com.warehouse.auth.domain.exception.UserDeletedException;
import com.warehouse.auth.domain.registry.DomainRegistry;
import com.warehouse.auth.domain.vo.UserDepartmentUpdateRequest;
import com.warehouse.auth.domain.vo.UserSnapshot;
import com.warehouse.commonassets.enumeration.AccountLevel;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.BelongsToOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


public abstract class OrganizationUser extends BelongsToOperator implements Account {

    private UserId userId;

    private String username;

    private String password;

    private String firstName;

    private String lastName;

    private String email;

    private DepartmentId departmentId;

    private String language;

    private String apiKey;

    private Set<RolePermission> permissions;

    private Boolean deleted;

    private Boolean initial;

    private Instant createdAt;

    private Instant updatedAt;

    protected OrganizationUser() {

    }

    public abstract Role getRole();

    public static OrganizationUser forRole(final Role role) {
        return switch (role) {
            case USER -> new User();
            case SUPPLIER -> new Supplier();
            case MANAGER -> new Manager();
            case ADMIN -> new Admin();
            case SUPERADMIN -> new Superadmin();
        };
    }

    public static OrganizationUser of(final UserId userId,
                                      final String username,
                                      final String password,
                                      final String firstName,
                                      final String lastName,
                                      final String email,
                                      final Role role,
                                      final DepartmentId departmentId,
                                      final String apiKey) {
        final OrganizationUser user = forRole(role);
        user.userId = userId;
        user.username = username;
        user.password = password;
        user.firstName = firstName;
        user.lastName = lastName;
        user.email = email;
        user.departmentId = departmentId;
        user.apiKey = apiKey;
        user.permissions = new HashSet<>();
        user.deleted = false;
        return user;
    }

    public static OrganizationUser createAdmin(final UserId userId,
                                               final String username,
                                               final String password,
                                               final String firstName,
                                               final String lastName,
                                               final String email,
                                               final DepartmentId departmentId,
                                               final String language,
                                               final String apiKey) {
        return createWithRole(userId, username, password, firstName, lastName, email, Role.ADMIN, departmentId,
                apiKey, language);
    }

    public static OrganizationUser createSuperadmin(final UserId userId,
                                                    final String username,
                                                    final String password,
                                                    final String firstName,
                                                    final String lastName,
                                                    final String email,
                                                    final DepartmentId departmentId,
                                                    final String language,
                                                    final String apiKey) {
        return createWithRole(userId, username, password, firstName, lastName, email, Role.SUPERADMIN, departmentId,
                apiKey, language);
    }

    public static OrganizationUser createWithRole(final UserId userId,
                                                  final String username,
                                                  final String password,
                                                  final String firstName,
                                                  final String lastName,
                                                  final String email,
                                                  final Role role,
                                                  final DepartmentId departmentId,
                                                  final String apiKey,
                                                  final String language) {
        final OrganizationUser user = of(userId, username, password, firstName, lastName, email, role, departmentId,
                apiKey);
        user.language = language;
        user.deleted = false;
        user.initial = false;
        user.createdAt = Instant.now();
        user.updatedAt = Instant.now();
        user.updatePermissionsForRole(role);
        return user;
    }

    @Override
    public AccountLevel getLevel() {
        return getRole().getLevel();
    }

    @Override
    public boolean canAccessOperatorPanel() {
        return false;
    }

    @Override
    public boolean canAccessDepartment(final DepartmentId departmentId) {
        return Objects.equals(this.departmentId, departmentId);
    }

    public boolean canManage(final OrganizationUser user) {
        return Objects.equals(operatorId(), user.operatorId())
                && canAccessDepartment(user.getDepartmentId())
                && canGrant(user.getLevel());
    }

    public boolean canAssignRole(final Role role) {
        return canGrant(role.getLevel());
    }

    protected boolean canGrant(final AccountLevel level) {
        return getLevel().isHigherThan(level);
    }

    public DepartmentId getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(final DepartmentId departmentId) {
        this.departmentId = departmentId;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    @Override
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String getLastName() {
        return lastName;
    }

    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    @Override
    public String getPassword() {
        return password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public void setUserId(final UserId userId) {
        this.userId = userId;
    }

    @Override
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

    public String getLanguage() {
        return language == null || language.isBlank() ? "pl" : language;
    }

    public void setLanguage(final String language) {
        this.language = language;
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

    public void setInitial(final Boolean initial) {
        this.initial = initial;
    }

    public void setUpdatedAt(final Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public Boolean isDeleted() {
        return deleted;
    }

    public Boolean isInitial() {
        return Boolean.TRUE.equals(initial);
    }

    public void markAsInitial() {
        this.initial = true;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public Instant updatedAt() {
        return updatedAt;
    }

    public Long getOperatorIdValue() {
        return this.operatorId().getValue();
    }

    public Set<RolePermission> getPermissions() {
        if (permissions == null) {
            this.permissions = new HashSet<>();
        }
        return permissions;
    }

    public UserSnapshot snapshot() {
        return new UserSnapshot(userId, username, password, email, getRole(), departmentId);
    }

    public Collection<? extends GrantedAuthority> getAuthorities() {

        final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

        if (permissions != null) {
            authorities.addAll(
                    permissions.stream().map(p -> new SimpleGrantedAuthority(p.getPermission().name())).collect(Collectors.toSet()));
        }

        return authorities;
    }

    public void changeFullName(final FullNameChangeCommand request) {
        if (StringUtils.isNotBlank(request.getFirstName())) {
            this.firstName = request.getFirstName();
        }
        if (StringUtils.isNotBlank(request.getLastName())) {
            this.lastName = request.getLastName();
        }
        markAsModified();
    }

    public void update(final UpdateUserCommand command, final DepartmentId departmentId) {
        this.firstName = command.firstName();
        this.lastName = command.lastName();
        this.username = command.username();
        this.email = command.email();
        this.departmentId = departmentId;
        this.language = command.language();
        markAsModified();
    }

    public void changePassword(final String encodedPassword) {
        this.password = encodedPassword;
        markAsModified();
    }

    public void changeLanguage(final String language) {
        this.language = language;
        markAsModified();
    }

    public void changeApiKey(final String apiKey) {
        this.apiKey = apiKey;
        markAsModified();
    }

    public OrganizationUser changeRole(final Role role) {
        if (Boolean.TRUE.equals(deleted)) {
            throw new UserDeletedException("User is already deleted");
        }

        final OrganizationUser user = forRole(role);
        user.copyStateFrom(this);
        user.updatePermissionsForRole(role);
        user.markAsModified();
        DomainRegistry.eventPublisher().publishEvent(new UserChangedEvent(user.snapshot()));
        return user;
    }

    private void copyStateFrom(final OrganizationUser source) {
        this.userId = source.userId;
        this.username = source.username;
        this.password = source.password;
        this.firstName = source.firstName;
        this.lastName = source.lastName;
        this.email = source.email;
        this.departmentId = source.departmentId;
        this.language = source.language;
        this.apiKey = source.apiKey;
        this.permissions = new HashSet<>(source.getPermissions());
        this.deleted = source.deleted;
        this.initial = source.initial;
        this.createdAt = source.createdAt;
        this.updatedAt = source.updatedAt;
        assignOperator(source.operatorId());
    }

    private void updatePermissionsForRole(final Role role) {
        getPermissions().clear();

        switch (role) {
            case SUPERADMIN, ADMIN -> getPermissions().addAll(DomainRegistry.rolePermissionService().findAllAdminPermissions());
            case MANAGER -> getPermissions().addAll(DomainRegistry.rolePermissionService().findAllManagerPermissions());
            case SUPPLIER -> getPermissions().addAll(DomainRegistry.rolePermissionService().findAllSupplierPermissions());
            case USER -> {}
        }
    }

    public void addPermission(final String permission) {
        final RolePermission rolePermission = DomainRegistry.rolePermissionService().findByName(permission);
        getPermissions().add(rolePermission);
        markAsModified();
    }

    public void removePermission(final String permission) {
        final RolePermission rolePermission = DomainRegistry.rolePermissionService().findByName(permission);
        getPermissions().remove(rolePermission);
        markAsModified();
    }

    public void anonymize(final String erasedPassword) {
        final String anonymousName = "deleted-" + (userId != null ? userId.value() : "user");
        this.username = anonymousName;
        this.firstName = "Deleted";
        this.lastName = "User";
        this.email = anonymousName + "@erased.invalid";
        this.password = erasedPassword;
        markAsDeleted();
    }

    public boolean hasApiKey() {
        return apiKey != null && !apiKey.isBlank();
    }

    public void markAsDeleted() {
        this.deleted = true;
        this.apiKey = null;
        getPermissions().clear();
        markAsModified();
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public boolean isAdmin() {
        return getLevel().isAtLeast(AccountLevel.ADMIN);
    }

    public void markAsLoggedOut() {
        markAsModified();
    }

    public void updateUserInfo(final UserDepartmentUpdateRequest request) {
        this.email = request.email();
        markAsModified();
    }

    public enum Role {
        USER(AccountLevel.USER),
        SUPPLIER(AccountLevel.USER),
        MANAGER(AccountLevel.MANAGER),
        ADMIN(AccountLevel.ADMIN),
        SUPERADMIN(AccountLevel.SUPERADMIN);

        private final AccountLevel level;

        Role(final AccountLevel level) {
            this.level = level;
        }

        public AccountLevel getLevel() {
            return level;
        }
    }
}
