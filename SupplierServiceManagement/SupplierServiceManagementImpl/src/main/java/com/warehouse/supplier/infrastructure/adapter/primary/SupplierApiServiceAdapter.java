package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.primary.SupplyPort;
import com.warehouse.supplier.domain.vo.SupplierCreateCommand;
import com.warehouse.supplier.domain.vo.SupplierCreateResponse;

import java.util.List;

public class SupplierApiServiceAdapter implements SupplierApiService {

    private final SupplyPort supplyPort;

    public SupplierApiServiceAdapter(final SupplyPort supplyPort) {
        this.supplyPort = supplyPort;
    }

    @Override
    public List<SupplierDetailsDto> findAll() {
        return supplyPort.getAllByCurrentDepartment()
                .stream()
                .map(this::map)
                .toList();
    }

    @Override
    public SupplierCodeDto create(final CreateSupplierApiRequest request) {
        final SupplierCreateCommand command = new SupplierCreateCommand(
                request.supplierCode() != null ? new SupplierCode(request.supplierCode().value()) : null,
                request.firstName(),
                request.lastName(),
                request.telephoneNumber(),
                new DepartmentCode(request.departmentCode()));
        final SupplierCreateResponse response = supplyPort.create(command);
        return new SupplierCodeDto(response.supplierCode().value());
    }

    private SupplierDetailsDto map(final Supplier supplier) {
        final DepartmentCode departmentCode = supplier.getDepartmentId() != null
                ? supplyPort.getDepartmentCode(supplier.getDepartmentId())
                : null;
        return new SupplierDetailsDto(
                supplier.getSupplierId() != null ? supplier.getSupplierId().value() : null,
                new SupplierCodeDto(supplier.supplierCode().value()),
                supplier.getFirstName(),
                supplier.getLastName(),
                supplier.getTelephoneNumber(),
                departmentCode != null ? departmentCode.getValue() : null,
                supplier.getStatus() != null ? supplier.getStatus().name() : null);
    }

    @Override
    public int anonymizeAll() {
        return supplyPort.anonymizeAll();
    }
}
