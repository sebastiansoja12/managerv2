package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DepartmentServiceAdapterTest {

    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);

    @Mock
    private DepartmentApiService departmentApiService;

    @Mock
    private OperatorContext operatorContext;

    private DepartmentServiceAdapter departmentServiceAdapter;

    @BeforeEach
    void setUp() {
        this.departmentServiceAdapter = new DepartmentServiceAdapter(this.departmentApiService, this.operatorContext);
    }

    @Test
    void shouldReturnDepartmentCodeOfArchivedDepartment() {
        when(this.departmentApiService.findDepartmentCode(DEPARTMENT_ID))
                .thenReturn(Optional.of(new DepartmentCode("KT1")));

        final DepartmentCode departmentCode = this.departmentServiceAdapter.getDepartmentCode(DEPARTMENT_ID);

        assertThat(departmentCode).isEqualTo(new DepartmentCode("KT1"));
    }

    @Test
    void shouldReturnNullWhenDepartmentDoesNotExist() {
        when(this.departmentApiService.findDepartmentCode(DEPARTMENT_ID)).thenReturn(Optional.empty());

        final DepartmentCode departmentCode = this.departmentServiceAdapter.getDepartmentCode(DEPARTMENT_ID);

        assertThat(departmentCode).isNull();
    }

    @Test
    void shouldReturnNullWhenUserHasNoDepartment() {
        final DepartmentCode departmentCode = this.departmentServiceAdapter.getDepartmentCode(null);

        assertThat(departmentCode).isNull();
        verifyNoInteractions(this.departmentApiService);
    }
}
