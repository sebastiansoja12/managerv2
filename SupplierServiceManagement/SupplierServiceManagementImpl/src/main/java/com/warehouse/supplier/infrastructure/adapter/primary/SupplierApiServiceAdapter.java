package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.supplier.api.dto.SupplierProfileDto;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.primary.SupplyPort;
import com.warehouse.supplier.domain.vo.SupplierCreateCommand;
import com.warehouse.supplier.domain.vo.SupplierCreateResponse;

import java.util.List;
import java.util.Optional;

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
                request.email(),
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

    @Override
    public Optional<SupplierProfileDto> findProfileById(final SupplierId supplierId) {
        return Optional.ofNullable(supplyPort.getOneById(supplierId)).map(this::mapProfile);
    }

    @Override
    public Optional<SupplierProfileDto> findProfileByCode(final SupplierCode supplierCode) {
        return Optional.ofNullable(supplyPort.getOneByCode(supplierCode)).map(this::mapProfile);
    }

    @Override
    public Optional<SupplierProfileDto> findProfileByUserId(final UserId userId) {
        return Optional.ofNullable(supplyPort.getOneByUserId(userId)).map(this::mapProfile);
    }

    @Override
    public List<SupplierProfileDto> findProfiles() {
        return supplyPort.getAllByCurrentDepartment()
                .stream()
                .map(this::mapProfile)
                .toList();
    }

    private SupplierProfileDto mapProfile(final Supplier supplier) {
        return new SupplierProfileDto(
                supplier.getSupplierId(),
                supplier.supplierCode(),
                supplier.getFirstName(),
                supplier.getLastName(),
                supplier.getDepartmentId(),
                supplier.getStatus() != null ? supplier.getStatus().name() : null);
    }
}
