package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class PickupPointCollectionPolicy {

    private final RecipientVerificationMethod verificationMethod;
    private final int pickupCodeLength;
    private final int pickupCodeValidityHours;
    private final int maxInvalidCodeAttempts;
    private final boolean allowAuthorizedPerson;
    private final int reminderAfterDays;
    private final boolean allowStorageExtension;
    private final int storageExtensionDays;

    public PickupPointCollectionPolicy(final RecipientVerificationMethod verificationMethod,
                                       final int pickupCodeLength,
                                       final int pickupCodeValidityHours,
                                       final int maxInvalidCodeAttempts,
                                       final boolean allowAuthorizedPerson,
                                       final int reminderAfterDays,
                                       final boolean allowStorageExtension,
                                       final int storageExtensionDays) {
        this.verificationMethod = verificationMethod;
        this.pickupCodeLength = pickupCodeLength;
        this.pickupCodeValidityHours = pickupCodeValidityHours;
        this.maxInvalidCodeAttempts = maxInvalidCodeAttempts;
        this.allowAuthorizedPerson = allowAuthorizedPerson;
        this.reminderAfterDays = reminderAfterDays;
        this.allowStorageExtension = allowStorageExtension;
        this.storageExtensionDays = storageExtensionDays;
    }

    public static PickupPointCollectionPolicy defaultConfiguration() {
        return new PickupPointCollectionPolicy(
                RecipientVerificationMethod.PICKUP_CODE,
                6,
                72,
                5,
                true,
                2,
                true,
                2
        );
    }

    public RecipientVerificationMethod getVerificationMethod() { return verificationMethod; }
    public int getPickupCodeLength() { return pickupCodeLength; }
    public int getPickupCodeValidityHours() { return pickupCodeValidityHours; }
    public int getMaxInvalidCodeAttempts() { return maxInvalidCodeAttempts; }
    public boolean isAllowAuthorizedPerson() { return allowAuthorizedPerson; }
    public int getReminderAfterDays() { return reminderAfterDays; }
    public boolean isAllowStorageExtension() { return allowStorageExtension; }
    public int getStorageExtensionDays() { return storageExtensionDays; }
}
