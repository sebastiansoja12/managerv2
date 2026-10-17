package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.primary.SupplyPort;
import com.warehouse.supplier.domain.vo.SupplierCreateCommand;
import com.warehouse.supplier.domain.vo.SupplierCreateResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class SupplierApiServiceAdapterTest {

    private final SupplyPort supplyPort = mock(SupplyPort.class);

    private final SupplierApiServiceAdapter adapter = new SupplierApiServiceAdapter(supplyPort);

    @Test
    void shouldMapSuppliersWithDepartmentCode() {
        final Supplier supplier = new Supplier(new SupplierId(9L), new SupplierCode("abcdef"), "Adam", "Nowak",
                "+48111222333");
        supplier.changeDepartment(new DepartmentId(10L));
        when(supplyPort.getAllByCurrentDepartment()).thenReturn(List.of(supplier));
        when(supplyPort.getDepartmentCode(new DepartmentId(10L))).thenReturn(new DepartmentCode("KT1"));

        final List<SupplierDetailsDto> suppliers = adapter.findAll();

        assertThat(suppliers).containsExactly(new SupplierDetailsDto(9L, new SupplierCodeDto("abcdef"), "Adam", "Nowak",
                "+48111222333", "KT1", "INACTIVE"));
    }

    @Test
    void shouldLeaveSupplierCodeEmptySoItIsGenerated() {
        when(supplyPort.create(any())).thenReturn(new SupplierCreateResponse(new SupplierCode("generated")));

        final SupplierCodeDto supplierCode = adapter.create(new CreateSupplierApiRequest(null, "Adam", "Nowak",
                "+48111222333", "KT1"));

        final ArgumentCaptor<SupplierCreateCommand> command = ArgumentCaptor.forClass(SupplierCreateCommand.class);
        verify(supplyPort).create(command.capture());
        assertThat(command.getValue().supplierCode()).isNull();
        assertThat(command.getValue().departmentCode()).isEqualTo(new DepartmentCode("KT1"));
        assertThat(supplierCode).isEqualTo(new SupplierCodeDto("generated"));
    }
}
