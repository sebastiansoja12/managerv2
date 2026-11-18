package com.warehouse.mcp.infrastructure.adapter.secondary;

import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.mcp.application.port.secondary.CourierServicePort;
import com.warehouse.mcp.domain.vo.Courier;
import com.warehouse.mcp.domain.vo.CourierChange;
import com.warehouse.mcp.domain.vo.CourierDraft;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.supplier.api.dto.SupplierProfileDto;

public class CourierServiceAdapter implements CourierServicePort {

    private final SupplierApiService supplierApiService;

    public CourierServiceAdapter(final SupplierApiService supplierApiService) {
        this.supplierApiService = supplierApiService;
    }

    @Override
    public List<Courier> findCouriers() {
        return this.supplierApiService.findAll().stream().map(this::map).toList();
    }

    @Override
    public Optional<SupplierId> findCourierId(final SupplierCode courierCode) {
        return this.supplierApiService.findProfileByCode(courierCode).map(SupplierProfileDto::supplierId);
    }

    @Override
    public SupplierCode create(final CourierDraft courier) {
        final SupplierCodeDto supplierCode = this.supplierApiService.create(new CreateSupplierApiRequest(
                courier.courierCode() != null ? new SupplierCodeDto(courier.courierCode().value()) : null,
                courier.firstName(), courier.lastName(), courier.telephoneNumber(),
                courier.departmentCode().value(), courier.email()));
        return new SupplierCode(supplierCode.value());
    }

    @Override
    public void update(final CourierChange change) {
        this.supplierApiService.updateBasicData(change.courierCode(), change.firstName(), change.lastName(),
                change.telephoneNumber());
    }

    @Override
    public void activate(final SupplierCode courierCode) {
        this.supplierApiService.activate(courierCode);
    }

    @Override
    public void deactivate(final SupplierCode courierCode) {
        this.supplierApiService.deactivate(courierCode);
    }

    private Courier map(final SupplierDetailsDto supplier) {
        return new Courier(
                supplier.supplierId() != null ? new SupplierId(supplier.supplierId()) : null,
                supplier.supplierCode() != null ? new SupplierCode(supplier.supplierCode().value()) : null,
                supplier.firstName(), supplier.lastName(), supplier.telephoneNumber(),
                supplier.departmentCode() != null ? new DepartmentCode(supplier.departmentCode()) : null,
                supplier.status());
    }
}
