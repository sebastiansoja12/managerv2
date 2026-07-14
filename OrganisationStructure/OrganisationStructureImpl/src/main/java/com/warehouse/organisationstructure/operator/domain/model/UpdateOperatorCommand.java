package com.warehouse.organisationstructure.operator.domain.model;

import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;

import java.time.LocalDate;

public record UpdateOperatorCommand(TaxId taxId,
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
                                    OperatorStatus status) {
}
