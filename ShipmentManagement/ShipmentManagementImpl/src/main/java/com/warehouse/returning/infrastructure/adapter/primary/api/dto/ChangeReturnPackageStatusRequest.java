package com.warehouse.returning.infrastructure.adapter.primary.api.dto;

import com.warehouse.commonassets.identificator.UserId;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record ChangeReturnPackageStatusRequest(
        @NotBlank
        @Pattern(
                regexp = "PROCESSING|COMPLETED|CANCELLED",
                message = "Return status must be PROCESSING, COMPLETED or CANCELLED"
        )
        String returnStatus,
        @NotNull UserId processedBy
) {
}
