package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
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
        when(operatorContextProvider.currentContext())
                .thenReturn(Optional.of(new OperatorDetails(new OperatorId(1L), null, departmentId)));
        when(departmentServicePort.getDepartmentCode(departmentId)).thenReturn(departmentCode);

        final Optional<DepartmentCode> result = adapter().currentDepartmentCode();

        assertThat(result).contains(departmentCode);
    }

    @Test
    void shouldReturnEmptyWhenContextHasNoDepartment() {
        when(operatorContextProvider.currentContext())
                .thenReturn(Optional.of(new OperatorDetails(new OperatorId(1L), null, null)));

        assertThat(adapter().currentDepartmentCode()).isEmpty();
        verify(departmentServicePort, never()).getDepartmentCode(any());
    }

    private OperatorContextDepartmentCodeAdapter adapter() {
        return new OperatorContextDepartmentCodeAdapter(operatorContextProvider, departmentServicePort);
    }
}
