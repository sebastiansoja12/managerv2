package com.warehouse.auth.infrastructure.adapter.primary.consent;

import jakarta.validation.constraints.Size;

public record RequestImpersonationConsentRequest(@Size(max = 500) String reason) {
}
