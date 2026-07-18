package com.warehouse.auth.infrastructure.adapter.secondary.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Instant;
import java.util.Set;

import org.junit.jupiter.api.Test;

import com.warehouse.auth.domain.model.RolePermission;
import com.warehouse.auth.domain.model.User;
import com.warehouse.auth.domain.vo.RolePermissionId;
import com.warehouse.auth.infrastructure.adapter.secondary.entity.UserEntity;
import com.warehouse.commonassets.enumeration.UserPermission;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;

class UserMapperTest {

    @Test
    void shouldMapUserToEntityWithOperatorAndFlags() {
        final User user = new User(new UserId(7L), "supplier", "password", "Supplier", "User",
                "supplier@test.pl", User.Role.SUPPLIER, new DepartmentCode("SUP"), "api-key", "PL",
                Set.of(new RolePermission(new RolePermissionId(5L), UserPermission.ROLE_SUPPLIER_READ)));
        user.assignOperator(OperatorId.of(200L));
        user.setInitial(true);

        final UserEntity entity = UserToEntityMapper.map(user);

        assertThat(entity.getUserId()).isEqualTo(new UserId(7L));
        assertThat(entity.operatorId()).isEqualTo(OperatorId.of(200L));
        assertThat(entity.getRole()).isEqualTo(UserEntity.Role.SUPPLIER);
        assertThat(entity.getInitial()).isTrue();
        assertThat(entity.getPermissions()).extracting("name").containsExactly("ROLE_SUPPLIER_READ");
    }

    @Test
    void shouldMapEntityToUserWithOperatorAndFlags() {
        final Instant createdAt = Instant.parse("2026-07-18T10:15:30Z");
        final Instant updatedAt = Instant.parse("2026-07-18T11:15:30Z");
        final UserEntity entity = UserEntity.builder()
                .userId(new UserId(8L))
                .username("manager")
                .password("password")
                .firstName("Manager")
                .lastName("User")
                .email("manager@test.pl")
                .role(UserEntity.Role.MANAGER)
                .departmentCode(new DepartmentCode("MAN"))
                .language("PL")
                .apiKey("api-key")
                .permissions(Set.of(new com.warehouse.auth.infrastructure.adapter.secondary.entity.RolePermissionEntity(
                        3L, "ROLE_MANAGER_READ")))
                .deleted(false)
                .initial(true)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
        entity.assignOperator(OperatorId.of(300L));

        final User user = UserToModelMapper.map(entity);

        assertThat(user.getUserId()).isEqualTo(new UserId(8L));
        assertThat(user.operatorId()).isEqualTo(OperatorId.of(300L));
        assertThat(user.getRole()).isEqualTo(User.Role.MANAGER);
        assertThat(user.isInitial()).isTrue();
        assertThat(user.createdAt()).isEqualTo(createdAt);
        assertThat(user.updatedAt()).isEqualTo(updatedAt);
        assertThat(user.getPermissions()).extracting(RolePermission::getPermission)
                .containsExactly(UserPermission.ROLE_MANAGER_READ);
    }
}
