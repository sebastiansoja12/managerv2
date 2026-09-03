package com.warehouse.auth.infrastructure.adapter.primary.consent;

import jakarta.validation.constraints.NotNull;

public record ApproveImpersonationConsentRequest(@NotNull Integer durationMinutes) {
}
