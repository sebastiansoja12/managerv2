package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OperatorContextDepartmentCodeAdapterTest {

    @Mock
    private OperatorContextProvider operatorContextProvider;

    @Mock
    private DepartmentServicePort departmentServicePort;

    @Test
    void shouldLoadDepartmentCodeForCurrentOperatorContext() {
        final DepartmentId departmentId = new DepartmentId(7L);
        final DepartmentCode departmentCode = new DepartmentCode("PO1");
        when(operatorContextProvider.currentDepartmentId()).thenReturn(departmentId);
        when(departmentServicePort.getDepartmentCode(departmentId)).thenReturn(departmentCode);

        final DepartmentCode result = adapter().currentDepartmentCode();

        assertThat(result).isEqualTo(departmentCode);
    }

    @Test
    void shouldRejectMissingDepartmentContext() {
        when(operatorContextProvider.currentDepartmentId())
                .thenThrow(new IllegalStateException("Department context is required"));

        assertThatThrownBy(() -> adapter().currentDepartmentCode())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("Department context is required");
    }

    private OperatorContextDepartmentCodeAdapter adapter() {
        return new OperatorContextDepartmentCodeAdapter(operatorContextProvider, departmentServicePort);
    }
}
