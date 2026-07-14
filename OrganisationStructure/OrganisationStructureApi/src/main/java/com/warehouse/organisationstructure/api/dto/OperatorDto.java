package com.warehouse.organisationstructure.api.dto;

import java.time.Instant;
import java.time.LocalDate;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;

public record OperatorDto(
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
        OperatorConfigurationDto configuration,
        OperatorStatusDto status,
        Instant createdAt,
        Instant updatedAt
) {
}
