package com.warehouse.supplier.domain.port.primary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.exception.SupplierAlreadyExistsException;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.secondary.DeviceServicePort;
import com.warehouse.supplier.domain.port.secondary.DepartmentServicePort;
import com.warehouse.supplier.domain.port.secondary.UserServicePort;
import com.warehouse.supplier.domain.service.DriverLicenseService;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.domain.service.SupplierValidatorService;
import com.warehouse.supplier.domain.vo.*;
import com.warehouse.supplier.infrastructure.adapter.secondary.exception.SupplierNotFoundException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;

import java.util.List;

public class SupplyPortImpl implements SupplyPort {

    private final SupplierService supplierService;

    private final SupplierCodeGeneratorService generatorService;

    private final SupplierValidatorService validatorService;

    private final DriverLicenseService driverLicenseService;

    private final DeviceServicePort deviceServicePort;

    private final DepartmentServicePort departmentServicePort;

    private final UserServicePort userServicePort;

    public SupplyPortImpl(final SupplierService supplierService,
                          final SupplierCodeGeneratorService generatorService,
                          final SupplierValidatorService validatorService,
                          final DriverLicenseService driverLicenseService,
                          final DeviceServicePort deviceServicePort,
                          final DepartmentServicePort departmentServicePort,
                          final UserServicePort userServicePort) {
        this.supplierService = supplierService;
        this.generatorService = generatorService;
        this.validatorService = validatorService;
        this.driverLicenseService = driverLicenseService;
        this.deviceServicePort = deviceServicePort;
        this.departmentServicePort = departmentServicePort;
        this.userServicePort = userServicePort;
    }

    @Override
    @Transactional
    public SupplierCreateResponse create(final SupplierCreateCommand supplierCreateCommand) {
        final SupplierCode supplierCode = this.generatorService.generate(supplierCreateCommand.supplierCode());
        validateNotExists(supplierCode);
        final String firstName = supplierCreateCommand.firstName();
        final String lastName = supplierCreateCommand.lastName();
        final String telephoneNumber = supplierCreateCommand.telephoneNumber();
        final String email = supplierCreateCommand.email();
        final DepartmentCode departmentCode = supplierCreateCommand.departmentCode();
        if (!validatorService.validateDepartmentExistsByDepartmentCode(departmentCode)) {
            throw new RestClientException("Department not found");
        }
        final SupplierId supplierId = this.supplierService.nextSupplierId();
        final DepartmentId departmentId = departmentServicePort.getDepartmentId(departmentCode);
        final Supplier supplier = new Supplier(supplierId, supplierCode, firstName, lastName, telephoneNumber, email,
                departmentId);
        supplier.markUserCreated(this.userServicePort.createUserForSupplier(supplier.snapshot()));

        this.supplierService.create(supplier);

        return new SupplierCreateResponse(supplier.supplierCode());
    }

    @Override
    public List<Supplier> getAllByCurrentDepartment() {
        return supplierService.findAllByCurrentDepartment();
    }

    @Override
    public Supplier getOneById(final SupplierId supplierId) {
        return supplierService.findById(supplierId);
    }

    @Override
    public Supplier getOneByCode(final SupplierCode supplierCode) {
        return supplierService.findByCode(supplierCode);
    }

    @Override
    public Supplier getOneByUserId(final UserId userId) {
        return supplierService.findByUserId(userId);
    }

    @Override
    public void activate(final SupplierCode supplierCode) {
        validateExists(supplierCode);
        supplierService.activate(supplierCode);
    }

    @Override
    public void deactivate(final SupplierCode supplierCode) {
        validateExists(supplierCode);
        supplierService.deactivate(supplierCode);
    }

    @Override
    public Result<Void, String> updateBasicData(final SupplierBasicDataUpdateCommand request) {
        final SupplierCode supplierCode = request.supplierCode();
        if (supplierService.findByCode(supplierCode) == null) {
            return Result.failure("Supplier with code " + supplierCode + " does not exist");
        }
        this.supplierService.updateBasicData(supplierCode, request.firstName(), request.lastName(),
                request.telephoneNumber());
        return Result.success();
    }

    @Override
    public void addPackageType(final ChangeSupportedPackageTypeCommand command) {
        validateExists(command.supplierCode());
        this.supplierService.changeSupportedPackageTypes(command.supplierCode(), command.supportedPackageTypes());
    }

    @Override
    public void addDevice(final ChangeSupplierDeviceCommand command) {
        final DeviceId deviceId = command.deviceId();
        final SupplierCode supplierCode = command.supplierCode();
        if (supplierService.findByCode(supplierCode) == null) {
            throw new SupplierNotFoundException(supplierCode.value());
        }
        this.supplierService.addDevice(supplierCode, deviceId);
    }

    @Override
    public void assignVehicle(final ChangeSupplierVehicleCommand command) {
        final SupplierCode supplierCode = command.supplierCode();
        validateExists(supplierCode);
        this.supplierService.assignVehicle(supplierCode, command.vehicleId());
    }

    @Override
    public void updateDeliveryArea(final ChangeSupplierDeliveryAreaCommand command) {
        final SupplierCode supplierCode = command.supplierCode();
        validateExists(supplierCode);
        this.supplierService.updateDeliveryArea(supplierCode, command.deliveryArea());
    }

    @Override
    public void changeDepartmentCode(final ChangeSupplierDepartmentCodeCommand command) {
        final SupplierCode supplierCode = command.supplierCode();
        final DepartmentCode departmentCode = command.departmentCode();
        final Boolean departmentExists = validatorService.validateDepartmentExistsByDepartmentCode(departmentCode);

        if (supplierService.findByCode(supplierCode) == null || !departmentExists) {
            throw new RestClientException("Supplier or department not found");
        }

        final DepartmentId departmentId = departmentServicePort.getDepartmentId(departmentCode);
        this.supplierService.changeDepartment(supplierCode, departmentId);
    }

    @Override
    public Result<Void, String> update(final SupplierUpdateCommand command) {
        final SupplierCode supplierCode = command.supplierCode();
        final Result<Void, String> validationResult = this.validatorService.validateSupplierForUpdate(command);
        if (validationResult.isFailure()) {
            return Result.failure(validationResult.getFailure());
        }
        final DepartmentId departmentId = departmentServicePort.getDepartmentId(command.departmentCode());
        this.supplierService.update(supplierCode, SupplierDto.from(command, departmentId));
        return Result.success();
    }

    @Override
    public DepartmentCode getDepartmentCode(final DepartmentId departmentId) {
        return departmentServicePort.getDepartmentCode(departmentId);
    }

    @Override
    public DriverLicenseResponse updateDriverLicense(final DriverLicenseCommand request) {
        final DriverLicense driverLicense = request.driverLicense();
        final SupplierCode supplierCode = request.supplierCode();
        final Result<Void, String> validationResult = this.driverLicenseService.validateDriverLicense(driverLicense);
        if (validationResult.isSuccess()) {
            this.supplierService.updateDriverLicense(supplierCode, driverLicense);
            return DriverLicenseResponse.ok();
        } else {
            return DriverLicenseResponse.failure(validationResult.getFailure());
        }
    }

    @Override
    public CertificationUpdateResponse updateCertification(final CertificationUpdateCommand request) {
        final SupplierCode supplierCode = request.supplierCode();
        final DangerousGoodCertification certification = request.dangerousGoodCertification();
        if (this.supplierService.findByCode(supplierCode) == null) {
            return CertificationUpdateResponse.failure("Supplier with code " + supplierCode + " does not exist");
        }

        this.supplierService.updateCertification(supplierCode, certification);
        return CertificationUpdateResponse.ok();
    }

    private void validateNotExists(final SupplierCode supplierCode) {
        final Result<Void, String> result = this.validatorService.validateSupplierCode(supplierCode);
        if (result.isFailure()) {
            throw new SupplierAlreadyExistsException(result.getFailure());
        }
    }

    private void validateExists(final SupplierCode supplierCode) {
        if (supplierService.findByCode(supplierCode) == null) {
            throw new SupplierNotFoundException(supplierCode.value());
        }
    }

    @Override
    public int anonymizeAll() {
        return supplierService.anonymizeAll();
    }
}
