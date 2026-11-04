package com.warehouse.organisationstructure.department.infrastructure.adapter.primary;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.dto.DepartmentDto;
import com.warehouse.organisationstructure.department.application.port.primary.DepartmentPort;
import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.model.Department;
import com.warehouse.organisationstructure.department.domain.vo.Address;
import com.warehouse.organisationstructure.department.domain.vo.TaxId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceAdapterTest {

    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);

    @Mock
    private DepartmentPort departmentPort;

    private DepartmentServiceAdapter departmentServiceAdapter;

    @BeforeEach
    void setUp() {
        this.departmentServiceAdapter = new DepartmentServiceAdapter(this.departmentPort);
    }

    @Test
    void shouldReturnNullWhenDepartmentDoesNotExist() {
        when(this.departmentPort.findByDepartmentId(DEPARTMENT_ID)).thenReturn(null);

        final DepartmentDto department = this.departmentServiceAdapter.getDepartmentById(DEPARTMENT_ID);

        assertThat(department).isNull();
    }

    @Test
    void shouldReturnDepartmentWithoutCoordinates() {
        when(this.departmentPort.findByDepartmentId(DEPARTMENT_ID)).thenReturn(departmentWithoutCoordinates());

        final DepartmentDto department = this.departmentServiceAdapter.getDepartmentById(DEPARTMENT_ID);

        assertThat(department.departmentCode()).isEqualTo("KT1");
        assertThat(department.coordinates()).isNull();
    }

    @Test
    void shouldReturnDepartmentCodeRegardlessOfDepartmentStatus() {
        when(this.departmentPort.findDepartmentCode(DEPARTMENT_ID)).thenReturn(Optional.of(new DepartmentCode("KT1")));

        final Optional<DepartmentCode> departmentCode = this.departmentServiceAdapter.findDepartmentCode(DEPARTMENT_ID);

        assertThat(departmentCode).contains(new DepartmentCode("KT1"));
    }

    private static Department departmentWithoutCoordinates() {
        return new Department(
                DEPARTMENT_ID,
                new DepartmentCode("KT1"),
                new Address("Gliwice", "Zwycięstwa 1", "44-100", CountryCode.PL),
                new TaxId("1234567890"),
                "123456789",
                "08:00-16:00",
                "kt1@example.com",
                DepartmentType.BRANCH,
                Department.Status.ARCHIVED,
                null,
                Instant.parse("2026-01-01T00:00:00Z"),
                Instant.parse("2026-01-01T00:00:00Z"),
                null,
                null,
                null,
                null,
                null);
    }
}
