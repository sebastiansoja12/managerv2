package com.warehouse.organisationstructure.department.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.organisationstructure.department.api.dto.CreateDepartmentApiRequest;
import com.warehouse.organisationstructure.department.api.dto.DepartmentCodeDto;
import com.warehouse.organisationstructure.department.api.dto.DepartmentTypeDto;
import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.model.DepartmentCreate;
import com.warehouse.organisationstructure.department.domain.model.DepartmentCreateCommand;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CreateDepartmentRequestMapperTest {

    @Test
    void shouldMapApiRequestToSingleDepartmentCreateCommand() {
        final CreateDepartmentApiRequest request = new CreateDepartmentApiRequest(new DepartmentCodeDto("KT2"),
                "Katowice", "Testowa 1", "40-001", "PL", "1234567890", "+48123456789", "08:00-16:00",
                "kt2@test.pl", DepartmentTypeDto.WAREHOUSE);

        final DepartmentCreateCommand command = RequestMapper.map(request);

        assertThat(command.getDepartments()).hasSize(1);
        final DepartmentCreate department = command.getDepartments().get(0);
        assertThat(department.getDepartmentCode()).isEqualTo(new DepartmentCode("KT2"));
        assertThat(department.getCountryCode()).isEqualTo(CountryCode.PL);
        assertThat(department.getTaxId()).isEqualTo("1234567890");
        assertThat(department.getDepartmentType()).isEqualTo(DepartmentType.WAREHOUSE);
    }
}
