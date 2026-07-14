package com.warehouse.organisationstructure.operator.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.organisationstructure.api.dto.CreateOperatorApiRequest;
import com.warehouse.organisationstructure.api.dto.UpdateOperatorApiRequest;
import com.warehouse.organisationstructure.operator.domain.model.CreateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.mapper.OperatorMapper;

public final class OperatorRequestMapper {

    private OperatorRequestMapper() {
    }

    public static CreateOperatorCommand toCommand(final CreateOperatorApiRequest request) {
        return new CreateOperatorCommand(
                request.userFirstName(),
                request.userLastName(),
                request.username(),
                request.password(),
                request.language(),
                request.email(),
                TaxId.of(request.taxId()),
                request.supportsLockers(),
                request.supportsInternationalShipping(),
                request.supportsCashOnDelivery(),
                request.contactPhone(),
                request.contactEmail(),
                request.companyName(),
                request.contractStartDate(),
                request.contractEndDate(),
                request.foundedDate(),
                OperatorMapper.toModelConfiguration(request.configuration()),
                toFirstDepartment(request.firstDepartment())
        );
    }

    public static UpdateOperatorCommand toCommand(final UpdateOperatorApiRequest request) {
        return new UpdateOperatorCommand(
                TaxId.of(request.taxId()),
                request.supportsLockers(),
                request.supportsInternationalShipping(),
                request.supportsCashOnDelivery(),
                request.contactPhone(),
                request.contactEmail(),
                request.companyName(),
                request.contractStartDate(),
                request.contractEndDate(),
                request.foundedDate(),
                OperatorMapper.toModelConfiguration(request.configuration()),
                request.status() == null ? OperatorStatus.ACTIVE : OperatorStatus.valueOf(request.status().name())
        );
    }

    private static CreateOperatorCommand.FirstDepartment toFirstDepartment(
            final CreateOperatorApiRequest.FirstDepartmentDto firstDepartment) {
        if (firstDepartment == null) {
            return null;
        }
        return new CreateOperatorCommand.FirstDepartment(
                firstDepartment.departmentCode(),
                firstDepartment.city(),
                firstDepartment.street(),
                firstDepartment.postalCode(),
                firstDepartment.countryCode(),
                firstDepartment.openingHours(),
                firstDepartment.departmentType()
        );
    }
}
