package com.warehouse.platformadministration.infrastructure.adapter.secondary.mapper;

import com.warehouse.auth.infrastructure.dto.CreateOperatorUserApiRequest;
import com.warehouse.auth.infrastructure.dto.OperatorIdDto;
import com.warehouse.auth.infrastructure.dto.UserDto;
import com.warehouse.auth.infrastructure.dto.UserIdDto;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.department.api.dto.CreateDepartmentApiRequest;
import com.warehouse.organisationstructure.department.api.dto.DepartmentCodeDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDetailsDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentIdDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentStatusDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentTypeDto;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorCourierCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorDepartmentCommand;
import com.warehouse.platformadministration.application.port.primary.command.CreateOperatorUserCommand;
import com.warehouse.platformadministration.domain.vo.OperatorCourier;
import com.warehouse.platformadministration.domain.vo.OperatorDepartment;
import com.warehouse.platformadministration.domain.vo.OperatorUser;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OperatorDirectoryServiceMapperTest {

    @Test
    void shouldMapUserFromAuthorizationApi() {
        final UserDto userDto = new UserDto(new UserIdDto(5L), "jkowalski", "Jan", "Kowalski", "jan@test.pl",
                "SUPERADMIN", "KT1", "pl", Set.of(), false, new OperatorIdDto(1L), null, null);

        final OperatorUser user = OperatorDirectoryServiceMapper.toOperatorUser(userDto);

        assertThat(user.userId()).isEqualTo(new UserId(5L));
        assertThat(user.role()).isEqualTo("SUPERADMIN");
        assertThat(user.departmentCode()).isEqualTo(new DepartmentCode("KT1"));
        assertThat(user.deleted()).isFalse();
    }

    @Test
    void shouldMapCreateUserCommandToAuthorizationRequest() {
        final CreateOperatorUserCommand command = new CreateOperatorUserCommand("Jan", "Kowalski", "jkowalski",
                "secret", "jan@test.pl", "ADMIN", new DepartmentCode("KT1"), "pl");

        final CreateOperatorUserApiRequest request = OperatorDirectoryServiceMapper.toCreateOperatorUserApiRequest(command);

        assertThat(request.departmentCode()).isEqualTo("KT1");
        assertThat(request.role()).isEqualTo("ADMIN");
        assertThat(request.password()).isEqualTo("secret");
    }

    @Test
    void shouldMapDepartmentDetails() {
        final DepartmentDetailsDto departmentDto = new DepartmentDetailsDto(new DepartmentIdDto(3L),
                new DepartmentCodeDto("KT2"), "Katowice", "Testowa 1", "40-001", "PL", "1234567890", "+48123456789",
                "08:00-16:00", "kt2@test.pl", DepartmentTypeDto.BRANCH, DepartmentStatusDto.ACTIVE);

        final OperatorDepartment department = OperatorDirectoryServiceMapper.toOperatorDepartment(departmentDto);

        assertThat(department.departmentCode()).isEqualTo(new DepartmentCode("KT2"));
        assertThat(department.departmentType()).isEqualTo("BRANCH");
        assertThat(department.status()).isEqualTo("ACTIVE");
    }

    @Test
    void shouldMapCreateDepartmentCommand() {
        final CreateOperatorDepartmentCommand command = departmentCommand("WAREHOUSE");

        final CreateDepartmentApiRequest request = OperatorDirectoryServiceMapper.toCreateDepartmentApiRequest(command);

        assertThat(request.departmentCode()).isEqualTo(new DepartmentCodeDto("KT2"));
        assertThat(request.departmentType()).isEqualTo(DepartmentTypeDto.WAREHOUSE);
    }

    @Test
    void shouldRejectUnknownDepartmentType() {
        final CreateOperatorDepartmentCommand command = departmentCommand("CASTLE");

        assertThrows(IllegalArgumentException.class,
                () -> OperatorDirectoryServiceMapper.toCreateDepartmentApiRequest(command));
    }

    @Test
    void shouldMapCourierAndKeepMissingSupplierCodeEmpty() {
        final SupplierDetailsDto supplierDto = new SupplierDetailsDto(9L, new SupplierCodeDto("abcdef"), "Adam", "Nowak",
                "+48111222333", "KT1", "ACTIVE");
        final CreateOperatorCourierCommand command = new CreateOperatorCourierCommand(null, "Adam", "Nowak",
                "+48111222333", new DepartmentCode("KT1"));

        final OperatorCourier courier = OperatorDirectoryServiceMapper.toOperatorCourier(supplierDto);
        final CreateSupplierApiRequest request = OperatorDirectoryServiceMapper.toCreateSupplierApiRequest(command);

        assertThat(courier.supplierCode()).isEqualTo(new SupplierCode("abcdef"));
        assertThat(courier.departmentCode()).isEqualTo(new DepartmentCode("KT1"));
        assertThat(request.supplierCode()).isNull();
        assertThat(request.departmentCode()).isEqualTo("KT1");
    }

    private CreateOperatorDepartmentCommand departmentCommand(final String departmentType) {
        return new CreateOperatorDepartmentCommand(new DepartmentCode("KT2"), "Katowice", "Testowa 1", "40-001", "PL",
                "1234567890", "+48123456789", "08:00-16:00", "kt2@test.pl", departmentType);
    }
}
