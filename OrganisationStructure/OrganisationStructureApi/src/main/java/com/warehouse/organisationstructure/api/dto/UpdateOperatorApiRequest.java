package com.warehouse.organisationstructure.api.dto;

import java.time.LocalDate;

public record UpdateOperatorApiRequest(String taxId,
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
                                       OperatorStatusDto status) {
}
