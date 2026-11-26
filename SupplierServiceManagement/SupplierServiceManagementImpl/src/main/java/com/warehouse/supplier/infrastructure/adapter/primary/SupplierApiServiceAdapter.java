package com.warehouse.supplier.infrastructure.adapter.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.DeliveryAreaCourierDto;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDeliveryAreaDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.supplier.api.dto.SupplierProfileDto;
import com.warehouse.supplier.application.port.primary.SupplyPort;
import com.warehouse.supplier.application.port.primary.command.SupplierCreateCommand;
import com.warehouse.supplier.application.port.primary.result.SupplierCreateResponse;
import com.warehouse.supplier.infrastructure.adapter.primary.mapper.SupplierApiServiceMapper;

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
                .map(SupplierApiServiceMapper::mapDetails)
                .toList();
    }

    @Override
    public SupplierCodeDto create(final CreateSupplierApiRequest request) {
        final SupplierCreateCommand command = SupplierApiServiceMapper.map(request);
        final SupplierCreateResponse response = supplyPort.create(command);
        return SupplierApiServiceMapper.map(response);
    }

    @Override
    public void updateBasicData(final SupplierCode supplierCode, final String firstName, final String lastName,
                                final String telephoneNumber) {
        final Result<Void, String> result = supplyPort.updateBasicData(
                SupplierApiServiceMapper.map(supplierCode, firstName, lastName, telephoneNumber));
        if (result.isFailure()) {
            throw new IllegalArgumentException(result.getFailure());
        }
    }

    @Override
    public void activate(final SupplierCode supplierCode) {
        supplyPort.activate(supplierCode);
    }

    @Override
    public void deactivate(final SupplierCode supplierCode) {
        supplyPort.deactivate(supplierCode);
    }

    @Override
    public void assignVehicle(final SupplierCode supplierCode, final VehicleId vehicleId) {
        supplyPort.assignVehicle(SupplierApiServiceMapper.map(supplierCode, vehicleId));
    }

    @Override
    public void changeDeliveryArea(final SupplierCode supplierCode, final SupplierDeliveryAreaDto deliveryArea) {
        supplyPort.updateDeliveryArea(SupplierApiServiceMapper.map(supplierCode, deliveryArea));
    }

    @Override
    public int anonymizeAll() {
        return supplyPort.anonymizeAll();
    }

    @Override
    public Optional<SupplierProfileDto> findProfileById(final SupplierId supplierId) {
        return supplyPort.getOneById(supplierId).map(SupplierApiServiceMapper::mapProfile);
    }

    @Override
    public Optional<SupplierProfileDto> findProfileByCode(final SupplierCode supplierCode) {
        return supplyPort.getOneByCode(supplierCode).map(SupplierApiServiceMapper::mapProfile);
    }

    @Override
    public Optional<SupplierProfileDto> findProfileByUserId(final UserId userId) {
        return supplyPort.getOneByUserId(userId).map(SupplierApiServiceMapper::mapProfile);
    }

    @Override
    public List<DeliveryAreaCourierDto> findDeliveryAreaCouriers(final DepartmentId departmentId) {
        return supplyPort.getDeliveryAreaCouriers(departmentId)
                .stream()
                .map(SupplierApiServiceMapper::mapDeliveryAreaCourier)
                .toList();
    }

    @Override
    public List<SupplierProfileDto> findProfiles() {
        return supplyPort.getAllByCurrentDepartment()
                .stream()
                .map(SupplierApiServiceMapper::mapProfile)
                .toList();
    }
}
