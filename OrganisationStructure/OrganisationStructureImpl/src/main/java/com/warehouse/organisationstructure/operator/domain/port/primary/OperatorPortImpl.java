package com.warehouse.organisationstructure.operator.domain.port.primary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operator.domain.model.CreateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorProvisioningDetails;

import java.time.Instant;
import java.util.List;

public class OperatorPortImpl implements OperatorPort {

    private final OperatorService operatorService;

    public OperatorPortImpl(final OperatorService operatorService) {
        this.operatorService = operatorService;
    }

    @Override
    public List<Operator> getAll() {
        return operatorService.getAll();
    }

    @Override
    public Operator getById(final OperatorId operatorId) {
        return operatorService.getById(operatorId);
    }

    @Override
    public boolean exists(final OperatorId operatorId) {
        return operatorService.exists(operatorId);
    }

    @Override
    public boolean isActive(final OperatorId operatorId) {
        return operatorService.isActive(operatorId);
    }

    @Override
    public OperatorId create(final CreateOperatorCommand command) {
        validateCreateCommand(command);
        final Instant now = Instant.now();
        final OperatorId operatorId = operatorService.nextOperatorId();
        final OperatorProvisioningDetails provisioningDetails = toProvisioningDetails(command);
        final Operator operator = new Operator(operatorId, null, command.taxId(), command.supportsLockers(),
                command.supportsInternationalShipping(), command.supportsCashOnDelivery(), command.contactPhone(),
                command.contactEmail(), command.companyName(), command.contractStartDate(), command.contractEndDate(),
                command.foundedDate(), command.configuration(), provisioningDetails, OperatorStatus.ACTIVE, now, now);
        operatorService.create(operator);
        return operatorId;
    }

    @Override
    public Operator update(final OperatorId operatorId, final UpdateOperatorCommand command) {
        validateUpdateCommand(command);
        return operatorService.update(operatorId, command);
    }

    private void validateCreateCommand(final CreateOperatorCommand command) {
        if (command.firstDepartment() == null) {
            throw new IllegalArgumentException("First department data is required to create operator");
        }
        if (command.firstDepartment().departmentCode() == null || command.firstDepartment().departmentCode().isBlank()) {
            throw new IllegalArgumentException("First department code is required to create operator");
        }
    }

    private void validateUpdateCommand(final UpdateOperatorCommand command) {
        if (command.taxId() == null) {
            throw new IllegalArgumentException("Tax id is required to update operator");
        }
        if (command.companyName() == null || command.companyName().isBlank()) {
            throw new IllegalArgumentException("Company name is required to update operator");
        }
        if (command.configuration() == null) {
            throw new IllegalArgumentException("Operator configuration is required to update operator");
        }
    }

    private OperatorProvisioningDetails toProvisioningDetails(final CreateOperatorCommand command) {
        final CreateOperatorCommand.FirstDepartment firstDepartment = command.firstDepartment();
        final OperatorProvisioningDetails.FirstDepartment provisioningDepartment =
                new OperatorProvisioningDetails.FirstDepartment(
                        firstDepartment.departmentCode(),
                        firstDepartment.city(),
                        firstDepartment.street(),
                        firstDepartment.postalCode(),
                        firstDepartment.countryCode(),
                        firstDepartment.openingHours(),
                        firstDepartment.departmentType()
                );
        return new OperatorProvisioningDetails(
                command.userFirstName(),
                command.userLastName(),
                command.username(),
                command.password(),
                command.language(),
                command.email(),
                provisioningDepartment
        );
    }
}
