package com.warehouse.supplier.api;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.supplier.api.dto.CreateSupplierApiRequest;
import com.warehouse.supplier.api.dto.DeliveryAreaCourierDto;
import com.warehouse.supplier.api.dto.SupplierCodeDto;
import com.warehouse.supplier.api.dto.SupplierDeliveryAreaDto;
import com.warehouse.supplier.api.dto.SupplierDetailsDto;
import com.warehouse.supplier.api.dto.SupplierProfileDto;

import java.util.List;
import java.util.Optional;

public interface SupplierApiService {
    List<SupplierDetailsDto> findAll();
    SupplierCodeDto create(final CreateSupplierApiRequest request);

    int anonymizeAll();

    Optional<SupplierProfileDto> findProfileById(final SupplierId supplierId);
    Optional<SupplierProfileDto> findProfileByCode(final SupplierCode supplierCode);
    Optional<SupplierProfileDto> findProfileByUserId(final UserId userId);
    List<SupplierProfileDto> findProfiles();

    List<DeliveryAreaCourierDto> findDeliveryAreaCouriers(final DepartmentId departmentId);

    void updateBasicData(final SupplierCode supplierCode, final String firstName, final String lastName,
                         final String telephoneNumber);

    void activate(final SupplierCode supplierCode);

    void deactivate(final SupplierCode supplierCode);
    void assignVehicle(final SupplierCode supplierCode, final VehicleId vehicleId);
    void changeDeliveryArea(final SupplierCode supplierCode, final SupplierDeliveryAreaDto deliveryArea);
}
