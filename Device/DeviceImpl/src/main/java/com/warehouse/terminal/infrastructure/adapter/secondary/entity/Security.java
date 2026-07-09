package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class Security {

    @Column(name = "encrypted")
    private Boolean encrypted;

    @Column(name = "secure_boot_enabled")
    private Boolean secureBootEnabled;

    @Column(name = "tamper_detected")
    private Boolean tamperDetected;

    @Column(name = "screen_lock_enabled")
    private Boolean screenLockEnabled;

    @Column(name = "biometric_enabled")
    private Boolean biometricEnabled;

    @Column(name = "compromised")
    private Boolean compromised;

    @Column(name = "failed_login_attempts")
    private Integer failedLoginAttempts;

    @Column(name = "last_security_scan_at")
    private Instant lastSecurityScanAt;

    @Column(name = "security_policy_version")
    private String securityPolicyVersion;

    @Column(name = "certificate_fingerprint")
    private String certificateFingerprint;

    protected Security() {}

    public Security(final Boolean encrypted,
                    final Boolean secureBootEnabled,
                    final Boolean tamperDetected,
                    final Boolean screenLockEnabled,
                    final Boolean biometricEnabled,
                    final Boolean compromised,
                    final Integer failedLoginAttempts,
                    final Instant lastSecurityScanAt,
                    final String securityPolicyVersion,
                    final String certificateFingerprint) {
        this.encrypted = encrypted;
        this.secureBootEnabled = secureBootEnabled;
        this.tamperDetected = tamperDetected;
        this.screenLockEnabled = screenLockEnabled;
        this.biometricEnabled = biometricEnabled;
        this.compromised = compromised;
        this.failedLoginAttempts = failedLoginAttempts;
        this.lastSecurityScanAt = lastSecurityScanAt;
        this.securityPolicyVersion = securityPolicyVersion;
        this.certificateFingerprint = certificateFingerprint;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public Boolean getSecureBootEnabled() {
        return secureBootEnabled;
    }

    public Boolean getTamperDetected() {
        return tamperDetected;
    }

    public Boolean getScreenLockEnabled() {
        return screenLockEnabled;
    }

    public Boolean getBiometricEnabled() {
        return biometricEnabled;
    }

    public Boolean getCompromised() {
        return compromised;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public Instant getLastSecurityScanAt() {
        return lastSecurityScanAt;
    }

    public String getSecurityPolicyVersion() {
        return securityPolicyVersion;
    }

    public String getCertificateFingerprint() {
        return certificateFingerprint;
    }
}
