package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class UserEntity implements UserDetails {

    @Id
    @Column(name = "user_id", unique = true, nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId userId;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "department_code", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "department_code"))
    private DepartmentCode departmentCode;

    @Column(name = "api_key", nullable = false)
    private String apiKey;

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "users_role_permissions",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<RolePermissionEntity> permissions = new HashSet<>();

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {

		final Set<SimpleGrantedAuthority> authorities = new HashSet<>();

		if (permissions != null) {
			authorities.addAll(
					permissions.stream().map(p -> new SimpleGrantedAuthority("ROLE_" + p.getName())).collect(Collectors.toSet()));
		}

		return authorities;
	}

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Boolean isDeleted() {
        return deleted;
    }

    @RequiredArgsConstructor
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

        @Getter
        private final String permission;
    }


    @RequiredArgsConstructor
    public enum Role {

        USER(Collections.emptySet()),

        ADMIN(
                Set.of(
                        Permission.ROLE_ADMIN_READ,
                        Permission.ROLE_ADMIN_UPDATE,
                        Permission.ROLE_ADMIN_DELETE,
                        Permission.ROLE_ADMIN_CREATE,
                        Permission.ROLE_MANAGER_READ,
                        Permission.ROLE_MANAGER_UPDATE,
                        Permission.ROLE_MANAGER_DELETE,
                        Permission.ROLE_MANAGER_CREATE
                )
        ),

        MANAGER(
                Set.of(
                        Permission.ROLE_MANAGER_READ,
                        Permission.ROLE_MANAGER_UPDATE,
                        Permission.ROLE_MANAGER_DELETE,
                        Permission.ROLE_MANAGER_CREATE
                )
        ),

        SUPPLIER(
                Set.of(
                        Permission.ROLE_SUPPLIER_READ,
                        Permission.ROLE_SUPPLIER_UPDATE,
                        Permission.ROLE_SUPPLIER_DELETE,
                        Permission.ROLE_SUPPLIER_CREATE
                )
        );

        @Getter
        private final Set<Permission> permissions;
    }
}
