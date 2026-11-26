package com.warehouse.supplier.application.port.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.application.port.primary.command.CertificationUpdateCommand;
import com.warehouse.supplier.application.port.primary.command.ChangeSupplierDeliveryAreaCommand;
import com.warehouse.supplier.application.port.primary.command.ChangeSupplierDepartmentCodeCommand;
import com.warehouse.supplier.application.port.primary.command.ChangeSupplierDeviceCommand;
import com.warehouse.supplier.application.port.primary.command.ChangeSupplierVehicleCommand;
import com.warehouse.supplier.application.port.primary.command.ChangeSupportedPackageTypeCommand;
import com.warehouse.supplier.application.port.primary.command.DriverLicenseCommand;
import com.warehouse.supplier.application.port.primary.command.SupplierBasicDataUpdateCommand;
import com.warehouse.supplier.application.port.primary.command.SupplierCreateCommand;
import com.warehouse.supplier.application.port.primary.command.SupplierUpdateCommand;
import com.warehouse.supplier.application.port.primary.result.CertificationUpdateResponse;
import com.warehouse.supplier.application.port.primary.result.DriverLicenseResponse;
import com.warehouse.supplier.application.port.primary.result.SupplierCreateResponse;
import com.warehouse.supplier.application.port.primary.result.SupplierResult;

import com.warehouse.supplier.domain.vo.SupplierSnapshot;

import java.util.List;
import java.util.Optional;

public interface SupplyPort {

    SupplierCreateResponse create(final SupplierCreateCommand command);

    SupplierCreateResponse createInCurrentUserDepartment(final SupplierCreateCommand command);

    List<SupplierResult> getAllByCurrentDepartment();

    List<SupplierResult> getAllInCurrentUserDepartment();

    Optional<SupplierResult> getOneById(final SupplierId supplierId);

    Optional<SupplierResult> getOneByCode(final SupplierCode supplierCode);

    Optional<SupplierResult> getOneByUserId(final UserId userId);

    void activate(final SupplierCode supplierCode);

    void deactivate(final SupplierCode supplierCode);

    Result<Void, String> updateBasicData(final SupplierBasicDataUpdateCommand command);

    void addPackageType(final ChangeSupportedPackageTypeCommand command);

    void addDevice(final ChangeSupplierDeviceCommand command);

    void assignVehicle(final ChangeSupplierVehicleCommand command);

    void updateDeliveryArea(final ChangeSupplierDeliveryAreaCommand command);

    void changeDepartmentCode(final ChangeSupplierDepartmentCodeCommand command);

    Result<Void, String> update(final SupplierUpdateCommand command);

    DriverLicenseResponse updateDriverLicense(final DriverLicenseCommand command);

    CertificationUpdateResponse updateCertification(final CertificationUpdateCommand command);

    long countAssignedCouriers(final DepartmentId departmentId);

    List<SupplierSnapshot> getDeliveryAreaCouriers(final DepartmentId departmentId);

    void transferCouriers(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId);

    int anonymizeAll();
}
