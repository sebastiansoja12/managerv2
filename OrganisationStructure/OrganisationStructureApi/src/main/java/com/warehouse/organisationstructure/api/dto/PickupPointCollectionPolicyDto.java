package com.warehouse.organisationstructure.api.dto;

public record PickupPointCollectionPolicyDto(
        RecipientVerificationMethodDto verificationMethod,
        int pickupCodeLength,
        int pickupCodeValidityHours,
        int maxInvalidCodeAttempts,
        boolean allowAuthorizedPerson,
        int reminderAfterDays,
        boolean allowStorageExtension,
        int storageExtensionDays
) {
}
