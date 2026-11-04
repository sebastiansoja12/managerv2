package com.warehouse.organisationstructure.department.domain.service;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentLifecycleServicePort;
import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentRepository;
import com.warehouse.organisationstructure.department.application.port.secondary.DepartmentStatusPolicyServicePort;
import com.warehouse.organisationstructure.department.domain.enumeration.DepartmentType;
import com.warehouse.organisationstructure.department.domain.event.*;
import com.warehouse.organisationstructure.department.domain.model.Department;
import com.warehouse.organisationstructure.department.domain.registry.DomainRegistry;
import com.warehouse.organisationstructure.department.domain.vo.Address;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusChange;
import com.warehouse.organisationstructure.department.domain.vo.DepartmentStatusPolicy;
import com.warehouse.organisationstructure.department.domain.vo.TaxId;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Slf4j
public class DepartmentServiceImpl implements DepartmentService {

    private static final String MISSING_ACTIVATION_REQUIREMENTS_REASON = "Activation requirements not met: ";

    private static final String AUTOMATIC_ARCHIVE_REASON = "Archived automatically after %d days of inactivity";

    private final DepartmentRepository departmentRepository;

    private final DepartmentStatusPolicyServicePort departmentStatusPolicyServicePort;

    private final DepartmentLifecycleServicePort departmentLifecycleServicePort;

    private final WorkTransferTargetSelector workTransferTargetSelector;

    public DepartmentServiceImpl(final DepartmentRepository departmentRepository,
                                 final DepartmentStatusPolicyServicePort departmentStatusPolicyServicePort,
                                 final DepartmentLifecycleServicePort departmentLifecycleServicePort,
                                 final WorkTransferTargetSelector workTransferTargetSelector) {
        this.departmentRepository = departmentRepository;
        this.departmentStatusPolicyServicePort = departmentStatusPolicyServicePort;
        this.departmentLifecycleServicePort = departmentLifecycleServicePort;
        this.workTransferTargetSelector = workTransferTargetSelector;
    }

    @Override
    public void createDepartment(final Department department) {
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(
                new DepartmentCreated(department.snapshot(), Instant.now())
        );
        this.departmentLifecycleServicePort.notifyCreated(department.getDepartmentId());
        final List<String> activationBlockers =
                this.departmentLifecycleServicePort.activationBlockers(department.getDepartmentId());
        if (activationBlockers.isEmpty()) {
            return;
        }
        department.changeStatus(Department.Status.INACTIVE, new DepartmentStatusChange(department.getCreatedBy(),
                MISSING_ACTIVATION_REQUIREMENTS_REASON + String.join("; ", activationBlockers),
                DepartmentStatusPolicy.unrestricted(), List.of(), List.of()));
        save(department, Department.Status.INACTIVE);
    }

    @Override
    public Department findByDepartmentCode(final DepartmentCode departmentCode) {
        return departmentRepository.findByDepartmentCode(departmentCode);
    }

    @Override
    public Department findByDepartmentId(final DepartmentId departmentId) {
        return departmentRepository.findByDepartmentId(departmentId);
    }

    @Override
    public void changeAddress(final DepartmentCode departmentCode, final Address address) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeAddress(address);
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(new DepartmentAddressChanged(department.snapshot(), Instant.now()));
    }

    @Override
    public void changeTaxId(final DepartmentCode departmentCode, final TaxId newTaxId) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeTaxId(newTaxId);
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(new DepartmentTaxIdChanged(department.snapshot(), Instant.now()));
    }

    @Override
    public void activateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy) {
        changeStatus(this.departmentRepository.findByDepartmentCode(departmentCode), Department.Status.ACTIVE,
                null, modifiedBy);
    }

    @Override
    public void deactivateDepartment(final DepartmentCode departmentCode, final UserId modifiedBy,
                                     final String reason) {
        changeStatus(this.departmentRepository.findByDepartmentCode(departmentCode), Department.Status.INACTIVE,
                reason, modifiedBy);
    }

    @Override
    public void changeDepartmentType(final DepartmentCode departmentCode, final DepartmentType departmentType) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeDepartmentType(departmentType);
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(new DepartmentTypeChanged(department.snapshot(), Instant.now()));
    }

    @Override
    public void changeAdminUser(final DepartmentCode departmentCode, final UserId userId) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeAdminUserId(userId);
        this.departmentRepository.createOrUpdate(department);
    }

    @Override
    public void changeStatus(final DepartmentCode departmentCode, final Department.Status status,
                             final String reason, final UserId modifiedBy) {
        changeStatus(this.departmentRepository.findByDepartmentCodeIncludingArchived(departmentCode), status,
                reason, modifiedBy);
    }

    @Override
    public int archiveInactiveDepartments() {
        final Instant now = Instant.now();
        final List<Department> inactive = this.departmentRepository.findAll().stream()
                .filter(department -> department.getStatus() == Department.Status.INACTIVE)
                .toList();
        int archived = 0;
        for (final Department department : inactive) {
            final DepartmentStatusPolicy policy =
                    this.departmentStatusPolicyServicePort.policyFor(department.getDepartmentId());
            if (policy.autoArchiveInactiveAfterDays() <= 0 || policy.archivingPrevented()
                    || !inactiveSince(department).isBefore(
                            now.minus(Duration.ofDays(policy.autoArchiveInactiveAfterDays())))) {
                continue;
            }
            try {
                changeStatus(department, Department.Status.ARCHIVED,
                        AUTOMATIC_ARCHIVE_REASON.formatted(policy.autoArchiveInactiveAfterDays()), null);
                archived++;
            } catch (final RuntimeException exception) {
                log.warn("Department {} was not archived automatically: {}",
                        department.getDepartmentCode().getValue(), exception.getMessage());
            }
        }
        return archived;
    }

    @Override
    public void changeEmail(final DepartmentCode departmentCode, final String email) {
        final Department department = this.departmentRepository.findByDepartmentCode(departmentCode);
        department.changeEmail(email);
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(new DepartmentEmailChanged(department.snapshot(), Instant.now()));
    }

    @Override
    public Boolean checkExists(final DepartmentCode departmentCode) {
        return this.departmentRepository.checkExists(departmentCode);
    }

    private void changeStatus(final Department department, final Department.Status status, final String reason,
                              final UserId modifiedBy) {
        final DepartmentId departmentId = department.getDepartmentId();
        final DepartmentStatusPolicy policy = this.departmentStatusPolicyServicePort.policyFor(departmentId);
        if (status == Department.Status.ACTIVE) {
            department.changeStatus(status, new DepartmentStatusChange(modifiedBy, reason, policy,
                    this.departmentLifecycleServicePort.activationBlockers(departmentId), List.of()));
        } else {
            if (policy.automaticWorkTransfer()) {
                this.workTransferTargetSelector.select(department, this.departmentRepository.findAll())
                        .ifPresent(target -> this.departmentLifecycleServicePort.transferWork(departmentId,
                                target.getDepartmentId()));
            }
            department.changeStatus(status, new DepartmentStatusChange(modifiedBy, reason, policy, List.of(),
                    this.departmentLifecycleServicePort.activeWork(departmentId)));
        }
        save(department, status);
    }

    private void save(final Department department, final Department.Status status) {
        this.departmentRepository.createOrUpdate(department);
        DomainRegistry.eventPublisher().publishEvent(createDepartmentEvent(status, department));
    }

    private Instant inactiveSince(final Department department) {
        if (department.getStatusChangedAt() != null) {
            return department.getStatusChangedAt();
        }
        return department.getUpdatedAt() != null ? department.getUpdatedAt() : Instant.now();
    }

    private DepartmentEvent createDepartmentEvent(final Department.Status status, final Department department) {
        return switch (status) {
            case ACTIVE -> new DepartmentActivated(department.snapshot(), Instant.now());
            case INACTIVE -> new DepartmentDeactivated(department.snapshot(), Instant.now());
            case ARCHIVED -> new DepartmentArchived(department.snapshot(), Instant.now());
            case SUSPENDED -> new DepartmentSuspended(department.snapshot(), Instant.now());
            case DELETED -> new DepartmentDeleted(department.snapshot(), Instant.now());
        };
    }
}
