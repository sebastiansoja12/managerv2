package com.warehouse.auth.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.UserId;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

import static com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity.Permission.*;

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

    @RequiredArgsConstructor
    public enum Permission {

        ADMIN_READ("admin:read"),
        ADMIN_UPDATE("admin:update"),
        ADMIN_CREATE("admin:create"),
        ADMIN_DELETE("admin:delete"),
        MANAGER_READ("management:read"),
        MANAGER_UPDATE("management:update"),
        MANAGER_CREATE("management:create"),
        MANAGER_DELETE("management:delete"),

        SUPPLIER_READ("supplier:read"),
        SUPPLIER_UPDATE("supplier:update"),
        SUPPLIER_CREATE("supplier:create"),
        SUPPLIER_DELETE("supplier:delete");

        @Getter
        private final String permission;
    }

    @RequiredArgsConstructor
    public enum Role {

        USER(Collections.emptySet()),
        ADMIN(
                Set.of(
                        ADMIN_READ,
                        ADMIN_UPDATE,
                        ADMIN_DELETE,
                        ADMIN_CREATE,
                        MANAGER_READ,
                        MANAGER_UPDATE,
                        MANAGER_DELETE,
                        MANAGER_CREATE
                )
        ),
        MANAGER(
                Set.of(
                        MANAGER_READ,
                        MANAGER_UPDATE,
                        MANAGER_DELETE,
                        MANAGER_CREATE
                )
        ),

        SUPPLIER(
                Set.of(
                        SUPPLIER_READ,
                        SUPPLIER_UPDATE,
                        SUPPLIER_DELETE,
                        SUPPLIER_CREATE)
        );

        @Getter
        private final Set<Permission> permissions;

        public Set<SimpleGrantedAuthority> getAuthorities() {
            final Set<SimpleGrantedAuthority> authorities = new HashSet<>(this.getAuthorities());

            if (permissions != null) {
                authorities.addAll(
                        permissions.stream().map(p -> new SimpleGrantedAuthority(p.name())).collect(Collectors.toSet()));
            }

            return authorities;
        }
    }
}
