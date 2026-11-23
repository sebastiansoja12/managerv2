package com.warehouse.supplier.application.port.primary;

import com.warehouse.eventoutbox.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;
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
import com.warehouse.supplier.application.port.secondary.CourierLimitServicePort;
import com.warehouse.supplier.application.port.secondary.CurrentUserServicePort;
import com.warehouse.supplier.application.port.secondary.DepartmentServicePort;
import com.warehouse.supplier.application.port.secondary.SupplierRepository;
import com.warehouse.supplier.application.port.secondary.UserServicePort;
import com.warehouse.supplier.domain.event.SupplierActivated;
import com.warehouse.supplier.domain.event.SupplierAnonymized;
import com.warehouse.supplier.domain.event.SupplierCreated;
import com.warehouse.supplier.domain.event.SupplierDangerousGoodCertificationChanged;
import com.warehouse.supplier.domain.event.SupplierDeactivated;
import com.warehouse.supplier.domain.event.SupplierDeliveryAreaChanged;
import com.warehouse.supplier.domain.event.SupplierDepartmentChanged;
import com.warehouse.supplier.domain.event.SupplierDeviceAssigned;
import com.warehouse.supplier.domain.event.SupplierDriverLicenseUpdated;
import com.warehouse.supplier.domain.event.SupplierUpdated;
import com.warehouse.supplier.domain.event.SupplierVehicleAssigned;
import com.warehouse.supplier.domain.exception.DepartmentCourierLimitExceededException;
import com.warehouse.supplier.domain.exception.DepartmentNotFoundException;
import com.warehouse.supplier.domain.exception.SupplierAlreadyExistsException;
import com.warehouse.supplier.domain.exception.SupplierDepartmentAccessDeniedException;
import com.warehouse.supplier.domain.exception.SupplierNotFoundException;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.service.DriverLicenseService;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.vo.SupplierActivationData;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class SupplyPortImpl implements SupplyPort {

    private final SupplierRepository supplierRepository;

    private final SupplierCodeGeneratorService supplierCodeGeneratorService;

    private final DriverLicenseService driverLicenseService;

    private final DepartmentServicePort departmentServicePort;

    private final UserServicePort userServicePort;

    private final CourierLimitServicePort courierLimitServicePort;

    private final CurrentUserServicePort currentUserServicePort;

    private final DomainEventPublisher domainEventPublisher;

    public SupplyPortImpl(final SupplierRepository supplierRepository,
                          final SupplierCodeGeneratorService supplierCodeGeneratorService,
                          final DriverLicenseService driverLicenseService,
                          final DepartmentServicePort departmentServicePort,
                          final UserServicePort userServicePort,
                          final CourierLimitServicePort courierLimitServicePort,
                          final CurrentUserServicePort currentUserServicePort,
                          final DomainEventPublisher domainEventPublisher) {
        this.supplierRepository = supplierRepository;
        this.supplierCodeGeneratorService = supplierCodeGeneratorService;
        this.driverLicenseService = driverLicenseService;
        this.departmentServicePort = departmentServicePort;
        this.userServicePort = userServicePort;
        this.courierLimitServicePort = courierLimitServicePort;
        this.currentUserServicePort = currentUserServicePort;
        this.domainEventPublisher = domainEventPublisher;
    }

    @Override
    @Transactional
    public SupplierCreateResponse create(final SupplierCreateCommand command) {
        final SupplierCode supplierCode = this.supplierCodeGeneratorService.generate(command.supplierCode());
        if (this.supplierRepository.findByCode(supplierCode) != null) {
            throw new SupplierAlreadyExistsException("Supplier with code " + supplierCode + " already exists");
        }
        final DepartmentCode departmentCode = command.departmentCode();
        if (!departmentExists(departmentCode)) {
            throw new DepartmentNotFoundException("Department not found");
        }
        final DepartmentId departmentId = this.departmentServicePort.getDepartmentId(departmentCode);
        ensureCourierLimitNotReached(departmentId);
        final Supplier supplier = new Supplier(nextSupplierId(), supplierCode, command.firstName(),
                command.lastName(), command.telephoneNumber(), command.email(), departmentId);
        supplier.markUserCreated(this.userServicePort.createUserForSupplier(supplier.snapshot()));
        this.supplierRepository.create(supplier);
        this.domainEventPublisher.publish(new SupplierCreated(supplier.snapshot(), Instant.now()));
        return new SupplierCreateResponse(supplier.supplierCode());
    }

    @Override
    @Transactional
    public SupplierCreateResponse createInCurrentUserDepartment(final SupplierCreateCommand command) {
        final DepartmentCode departmentCode = command.departmentCode();
        if (departmentExists(departmentCode) && !this.currentUserServicePort
                .canAccessDepartment(this.departmentServicePort.getDepartmentId(departmentCode))) {
            throw new SupplierDepartmentAccessDeniedException("Courier cannot be created in another department");
        }
        return create(command);
    }

    @Override
    public List<SupplierResult> getAllByCurrentDepartment() {
        return toResults(this.supplierRepository.findAllByCurrentDepartment());
    }

    @Override
    public List<SupplierResult> getAllInCurrentUserDepartment() {
        return toResults(this.supplierRepository.findAllByCurrentDepartment()
                .stream()
                .filter(supplier -> this.currentUserServicePort.canAccessDepartment(supplier.getDepartmentId()))
                .toList());
    }

    @Override
    public Optional<SupplierResult> getOneById(final SupplierId supplierId) {
        return Optional.ofNullable(this.supplierRepository.findById(supplierId)).map(this::toResult);
    }

    @Override
    public Optional<SupplierResult> getOneByCode(final SupplierCode supplierCode) {
        return Optional.ofNullable(this.supplierRepository.findByCode(supplierCode)).map(this::toResult);
    }

    @Override
    public Optional<SupplierResult> getOneByUserId(final UserId userId) {
        return Optional.ofNullable(this.supplierRepository.findByUserId(userId)).map(this::toResult);
    }

    @Override
    public void activate(final SupplierCode supplierCode) {
        final Supplier supplier = loadSupplier(supplierCode);
        supplier.markAsActive();
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierActivated(supplier.snapshot(), Instant.now()));
    }

    @Override
    public void deactivate(final SupplierCode supplierCode) {
        final Supplier supplier = loadSupplier(supplierCode);
        supplier.markAsInactive();
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierDeactivated(supplier.snapshot(), Instant.now()));
    }

    @Override
    public Result<Void, String> updateBasicData(final SupplierBasicDataUpdateCommand command) {
        final SupplierCode supplierCode = command.supplierCode();
        final Supplier supplier = this.supplierRepository.findByCode(supplierCode);
        if (supplier == null) {
            return Result.failure("Supplier with code " + supplierCode + " does not exist");
        }
        supplier.changeBasicData(command.firstName(), command.lastName(), command.telephoneNumber());
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierUpdated(supplier.snapshot(), Instant.now()));
        return Result.success();
    }

    @Override
    public void addPackageType(final ChangeSupportedPackageTypeCommand command) {
        final Supplier supplier = loadSupplier(command.supplierCode());
        supplier.changeSupportedPackageTypes(command.supportedPackageTypes());
        this.supplierRepository.update(supplier);
    }

    @Override
    public void addDevice(final ChangeSupplierDeviceCommand command) {
        final Supplier supplier = loadSupplier(command.supplierCode());
        supplier.changeDeviceId(command.deviceId());
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierDeviceAssigned(supplier.snapshot(), Instant.now()));
    }

    @Override
    public void assignVehicle(final ChangeSupplierVehicleCommand command) {
        final Supplier supplier = loadSupplier(command.supplierCode());
        supplier.assignVehicle(command.vehicleId());
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierVehicleAssigned(supplier.snapshot(), Instant.now()));
    }

    @Override
    public void updateDeliveryArea(final ChangeSupplierDeliveryAreaCommand command) {
        final Supplier supplier = loadSupplier(command.supplierCode());
        supplier.changeDeliveryArea(command.deliveryArea());
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierDeliveryAreaChanged(supplier.snapshot(), Instant.now()));
    }

    @Override
    public void changeDepartmentCode(final ChangeSupplierDepartmentCodeCommand command) {
        final DepartmentCode departmentCode = command.departmentCode();
        final Supplier supplier = loadSupplier(command.supplierCode());
        if (!departmentExists(departmentCode)) {
            throw new DepartmentNotFoundException("Department not found");
        }
        final DepartmentId departmentId = this.departmentServicePort.getDepartmentId(departmentCode);
        if (!departmentId.equals(supplier.getDepartmentId())) {
            ensureCourierLimitNotReached(departmentId);
        }
        changeDepartment(supplier, departmentId);
    }

    @Override
    public Result<Void, String> update(final SupplierUpdateCommand command) {
        final SupplierCode supplierCode = command.supplierCode();
        final DepartmentCode departmentCode = command.departmentCode();
        if (!departmentExists(departmentCode)) {
            return Result.failure("Department with code " + departmentCode + " not found");
        }
        final Supplier supplier = this.supplierRepository.findByCode(supplierCode);
        if (supplier == null) {
            return Result.failure("Supplier with code " + supplierCode + " not found");
        }
        final DepartmentId departmentId = this.departmentServicePort.getDepartmentId(departmentCode);
        supplier.updateData(new SupplierActivationData(departmentId, command.vehicleId(), command.deviceId(),
                command.dangerousGoodCertification(), command.driverLicense(), command.deliveryArea(),
                command.supportedPackageTypes()));
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierUpdated(supplier.snapshot(), Instant.now()));
        return Result.success();
    }

    @Override
    public DriverLicenseResponse updateDriverLicense(final DriverLicenseCommand command) {
        final Result<Void, String> validationResult =
                this.driverLicenseService.validateDriverLicense(command.driverLicense());
        if (validationResult.isFailure()) {
            return DriverLicenseResponse.failure(validationResult.getFailure());
        }
        final Supplier supplier = loadSupplier(command.supplierCode());
        supplier.changeDriverLicense(command.driverLicense());
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierDriverLicenseUpdated(supplier.snapshot(), Instant.now()));
        return DriverLicenseResponse.ok();
    }

    @Override
    public CertificationUpdateResponse updateCertification(final CertificationUpdateCommand command) {
        final SupplierCode supplierCode = command.supplierCode();
        final Supplier supplier = this.supplierRepository.findByCode(supplierCode);
        if (supplier == null) {
            return CertificationUpdateResponse.failure("Supplier with code " + supplierCode + " does not exist");
        }
        supplier.changeDangerousGoodCertification(command.dangerousGoodCertification());
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(
                new SupplierDangerousGoodCertificationChanged(supplier.snapshot(), Instant.now()));
        return CertificationUpdateResponse.ok();
    }

    @Override
    public long countAssignedCouriers(final DepartmentId departmentId) {
        return this.supplierRepository.findAssignedToDepartment(departmentId).size();
    }

    @Override
    @Transactional
    public void transferCouriers(final DepartmentId sourceDepartmentId, final DepartmentId targetDepartmentId) {
        final int maxCouriers = this.courierLimitServicePort.maxCouriersPerDepartment(targetDepartmentId);
        int targetCouriers = this.supplierRepository.findAssignedToDepartment(targetDepartmentId).size();
        for (final Supplier supplier : this.supplierRepository.findAssignedToDepartment(sourceDepartmentId)) {
            if (maxCouriers > 0 && targetCouriers >= maxCouriers) {
                return;
            }
            changeDepartment(supplier, targetDepartmentId);
            targetCouriers++;
        }
    }

    @Override
    public int anonymizeAll() {
        final List<Supplier> suppliers = this.supplierRepository.findAllByCurrentDepartment();
        suppliers.forEach(supplier -> {
            supplier.anonymize();
            this.supplierRepository.update(supplier);
            this.domainEventPublisher.publish(new SupplierAnonymized(supplier.snapshot(), Instant.now()));
        });
        return suppliers.size();
    }

    private void changeDepartment(final Supplier supplier, final DepartmentId departmentId) {
        supplier.changeDepartment(departmentId);
        this.supplierRepository.update(supplier);
        this.domainEventPublisher.publish(new SupplierDepartmentChanged(supplier.snapshot(), Instant.now()));
    }

    private Supplier loadSupplier(final SupplierCode supplierCode) {
        final Supplier supplier = this.supplierRepository.findByCode(supplierCode);
        if (supplier == null) {
            throw new SupplierNotFoundException(supplierCode.value());
        }
        return supplier;
    }

    private boolean departmentExists(final DepartmentCode departmentCode) {
        return this.departmentServicePort.validateDepartmentCode(departmentCode).isSuccess();
    }

    private void ensureCourierLimitNotReached(final DepartmentId departmentId) {
        final int maxCouriers = this.courierLimitServicePort.maxCouriersPerDepartment(departmentId);
        if (maxCouriers > 0 && this.supplierRepository.findAssignedToDepartment(departmentId).size() >= maxCouriers) {
            throw new DepartmentCourierLimitExceededException("Department " + departmentId.getValue()
                    + " already has the maximum number of couriers (" + maxCouriers + ")");
        }
    }

    private SupplierId nextSupplierId() {
        return new SupplierId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    private SupplierResult toResult(final Supplier supplier) {
        return new SupplierResult(supplier.snapshot(), resolveDepartmentCode(supplier.getDepartmentId()));
    }

    private List<SupplierResult> toResults(final List<Supplier> suppliers) {
        final Map<DepartmentId, DepartmentCode> departmentCodes = new HashMap<>();
        return suppliers.stream()
                .map(supplier -> new SupplierResult(supplier.snapshot(), supplier.getDepartmentId() == null
                        ? null
                        : departmentCodes.computeIfAbsent(supplier.getDepartmentId(), this::resolveDepartmentCode)))
                .toList();
    }

    private DepartmentCode resolveDepartmentCode(final DepartmentId departmentId) {
        return departmentId == null ? null : this.departmentServicePort.getDepartmentCode(departmentId);
    }
}
