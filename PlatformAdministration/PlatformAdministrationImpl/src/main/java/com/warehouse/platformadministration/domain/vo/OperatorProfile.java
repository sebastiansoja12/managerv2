package com.warehouse.platformadministration.domain.vo;

import com.warehouse.commonassets.identificator.OperatorId;

import java.time.Instant;
import java.time.LocalDate;

public record OperatorProfile(OperatorId operatorId,
                              String companyName,
                              String taxId,
                              String contactEmail,
                              String contactPhone,
                              LocalDate contractStartDate,
                              LocalDate contractEndDate,
                              LocalDate foundedDate,
                              String status,
                              Instant createdAt) {

    public boolean blocked() {
        return "INACTIVE".equals(status);
    }
}
