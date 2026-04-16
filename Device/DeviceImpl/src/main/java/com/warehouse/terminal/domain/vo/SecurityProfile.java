package com.warehouse.terminal.domain.vo;

import java.time.Instant;

public class SecurityProfile {
	private Boolean encrypted;
	private Boolean secureBootEnabled;
	private Boolean tamperDetected;
	private Boolean screenLockEnabled;
	private Boolean biometricEnabled;
	private Boolean compromised;
	private Integer failedLoginAttempts;
	private Instant lastSecurityScanAt;
	private String securityPolicyVersion;
	private String certificateFingerprint;

    public SecurityProfile() {}

	public SecurityProfile(final Boolean biometricEnabled, final String certificateFingerprint,
			final Boolean compromised, final Boolean encrypted, final Integer failedLoginAttempts,
			final Instant lastSecurityScanAt, final Boolean screenLockEnabled, final Boolean secureBootEnabled,
			final String securityPolicyVersion, final Boolean tamperDetected) {
		this.biometricEnabled = biometricEnabled;
		this.certificateFingerprint = certificateFingerprint;
		this.compromised = compromised;
		this.encrypted = encrypted;
		this.failedLoginAttempts = failedLoginAttempts;
		this.lastSecurityScanAt = lastSecurityScanAt;
		this.screenLockEnabled = screenLockEnabled;
		this.secureBootEnabled = secureBootEnabled;
		this.securityPolicyVersion = securityPolicyVersion;
		this.tamperDetected = tamperDetected;
	}

    public Boolean getBiometricEnabled() {
        return biometricEnabled;
    }

    public void setBiometricEnabled(final Boolean biometricEnabled) {
        this.biometricEnabled = biometricEnabled;
    }

    public String getCertificateFingerprint() {
        return certificateFingerprint;
    }

    public void setCertificateFingerprint(final String certificateFingerprint) {
        this.certificateFingerprint = certificateFingerprint;
    }

    public Boolean getCompromised() {
        return compromised;
    }

    public void setCompromised(final Boolean compromised) {
        this.compromised = compromised;
    }

    public Boolean getEncrypted() {
        return encrypted;
    }

    public void setEncrypted(final Boolean encrypted) {
        this.encrypted = encrypted;
    }

    public Integer getFailedLoginAttempts() {
        return failedLoginAttempts;
    }

    public void setFailedLoginAttempts(final Integer failedLoginAttempts) {
        this.failedLoginAttempts = failedLoginAttempts;
    }

    public Instant getLastSecurityScanAt() {
        return lastSecurityScanAt;
    }

    public void setLastSecurityScanAt(final Instant lastSecurityScanAt) {
        this.lastSecurityScanAt = lastSecurityScanAt;
    }

    public Boolean getScreenLockEnabled() {
        return screenLockEnabled;
    }

    public void setScreenLockEnabled(final Boolean screenLockEnabled) {
        this.screenLockEnabled = screenLockEnabled;
    }

    public Boolean getSecureBootEnabled() {
        return secureBootEnabled;
    }

    public void setSecureBootEnabled(final Boolean secureBootEnabled) {
        this.secureBootEnabled = secureBootEnabled;
    }

    public String getSecurityPolicyVersion() {
        return securityPolicyVersion;
    }

    public void setSecurityPolicyVersion(final String securityPolicyVersion) {
        this.securityPolicyVersion = securityPolicyVersion;
    }

    public Boolean getTamperDetected() {
        return tamperDetected;
    }

    public void setTamperDetected(final Boolean tamperDetected) {
        this.tamperDetected = tamperDetected;
    }

    public static SecurityProfile empty() {
        return new SecurityProfile(
                false,
                null,
                false,
                false,
                0,
                null,
                false,
                false,
                null,
                false
        );
    }

    public void update(final SecurityProfile security) {
        if (security.getEncrypted() != null) {
            this.encrypted = security.getEncrypted();
        }
        if (security.getSecureBootEnabled() != null) {
            this.secureBootEnabled = security.getSecureBootEnabled();
        }
        if (security.getTamperDetected() != null) {
            this.tamperDetected = security.getTamperDetected();
        }
        if (security.getScreenLockEnabled() != null) {
            this.screenLockEnabled = security.getScreenLockEnabled();
        }
        if (security.getBiometricEnabled() != null) {
            this.biometricEnabled = security.getBiometricEnabled();
        }
        if (security.getCompromised() != null) {
            this.compromised = security.getCompromised();
        }
        if (security.getFailedLoginAttempts() != null) {
            this.failedLoginAttempts = security.getFailedLoginAttempts();
        }
        if (security.getLastSecurityScanAt() != null) {
            this.lastSecurityScanAt = security.getLastSecurityScanAt();
        }
        if (security.getSecurityPolicyVersion() != null) {
            this.securityPolicyVersion = security.getSecurityPolicyVersion();
        }
        if (security.getCertificateFingerprint() != null) {
            this.certificateFingerprint = security.getCertificateFingerprint();
        }
    }

}
