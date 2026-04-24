package com.warehouse.terminal.request;

import java.time.Instant;

public record DeviceSecurityRequestDto(
        Boolean encrypted,
        Boolean secureBootEnabled,
        Boolean tamperDetected,
        Boolean screenLockEnabled,
        Boolean biometricEnabled,
        Boolean compromised,
        Integer failedLoginAttempts,
        Instant lastSecurityScanAt,
        String securityPolicyVersion,
        String certificateFingerprint
) {
}
