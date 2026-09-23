package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateReturnPackageRequest(
        @NotNull ShipmentId shipmentId,
        @NotBlank String reason,
        @NotNull DepartmentId departmentId,
        @NotBlank
        @Pattern(
                regexp = "DAMAGED|WRONG_ITEM|NO_LONGER_NEEDED|RECIPIENT_REFUSED|RECIPIENT_UNAVAILABLE|INVALID_ADDRESS|UNCLAIMED|SENDER_REQUESTED|OTHER",
                message = "Unsupported return reason code"
        )
        String reasonCode
) {
}
