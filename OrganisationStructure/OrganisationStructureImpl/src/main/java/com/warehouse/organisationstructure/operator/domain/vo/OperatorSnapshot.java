package com.warehouse.organisationstructure.operator.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.operator.domain.model.OperatorStatus;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.time.Instant;
import java.time.LocalDate;

public record OperatorSnapshot(
        OperatorId operatorId,
        UserId registeringUserId,
        TaxId taxId,
        boolean supportsLockers,
        boolean supportsInternationalShipping,
        boolean supportsCashOnDelivery,
        String contactPhone,
        String contactEmail,
        String companyName,
        LocalDate contractStartDate,
        LocalDate contractEndDate,
        LocalDate foundedDate,
        OperatorConfiguration configuration,
        OperatorProvisioningDetails provisioningDetails,
        OperatorStatus status,
        Instant createdAt,
        Instant updatedAt
) {
}
