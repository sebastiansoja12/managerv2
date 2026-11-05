package com.warehouse.auth.infrastructure.adapter.primary.mapper;

import com.warehouse.auth.domain.model.OrganizationUser;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ResponseMapperTest {

    private final ResponseMapper responseMapper = new ResponseMapper();

    @Test
    void shouldMapUserWithDepartmentCode() {
        final UserDto userDto = this.responseMapper.map(user(), new DepartmentCode("KT1"));

        assertThat(userDto.departmentCode()).isEqualTo("KT1");
    }

    @Test
    void shouldMapUserWhenDepartmentCodeIsMissing() {
        final UserDto userDto = this.responseMapper.map(user(), null);

        assertThat(userDto.username()).isEqualTo("s-soja");
        assertThat(userDto.departmentCode()).isNull();
    }

    private static OrganizationUser user() {
        final OrganizationUser user = OrganizationUser.of(new UserId(1L), "s-soja", "password", "Sebastian", "Soja",
                "a@a.pl", OrganizationUser.Role.USER, new DepartmentId(10L), "");
        user.assignOperator(OperatorId.of(1L));
        return user;
    }
}
