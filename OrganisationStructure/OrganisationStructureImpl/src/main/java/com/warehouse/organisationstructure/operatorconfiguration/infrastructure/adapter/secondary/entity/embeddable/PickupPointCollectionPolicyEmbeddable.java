package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.PickupPointCollectionPolicy;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.RecipientVerificationMethod;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class PickupPointCollectionPolicyEmbeddable {

    @Enumerated(EnumType.STRING)
    @Column(name = "pickup_point_verification_method")
    private RecipientVerificationMethod verificationMethod;

    @Column(name = "pickup_point_pickup_code_length")
    private int pickupCodeLength;

    @Column(name = "pickup_point_pickup_code_validity_hours")
    private int pickupCodeValidityHours;

    @Column(name = "pickup_point_max_invalid_code_attempts")
    private int maxInvalidCodeAttempts;

    @Column(name = "pickup_point_allow_authorized_person")
    private boolean allowAuthorizedPerson;

    @Column(name = "pickup_point_reminder_after_days")
    private int reminderAfterDays;

    @Column(name = "pickup_point_allow_storage_extension")
    private boolean allowStorageExtension;

    @Column(name = "pickup_point_storage_extension_days")
    private int storageExtensionDays;

    public PickupPointCollectionPolicyEmbeddable() {
    }

    public static PickupPointCollectionPolicyEmbeddable from(final PickupPointCollectionPolicy configuration) {
        final PickupPointCollectionPolicy source = configuration != null
                ? configuration
                : PickupPointCollectionPolicy.defaultConfiguration();
        final PickupPointCollectionPolicyEmbeddable embeddable = new PickupPointCollectionPolicyEmbeddable();
        embeddable.verificationMethod = source.getVerificationMethod();
        embeddable.pickupCodeLength = source.getPickupCodeLength();
        embeddable.pickupCodeValidityHours = source.getPickupCodeValidityHours();
        embeddable.maxInvalidCodeAttempts = source.getMaxInvalidCodeAttempts();
        embeddable.allowAuthorizedPerson = source.isAllowAuthorizedPerson();
        embeddable.reminderAfterDays = source.getReminderAfterDays();
        embeddable.allowStorageExtension = source.isAllowStorageExtension();
        embeddable.storageExtensionDays = source.getStorageExtensionDays();
        return embeddable;
    }

    public PickupPointCollectionPolicy toModel() {
        return new PickupPointCollectionPolicy(
                verificationMethod,
                pickupCodeLength,
                pickupCodeValidityHours,
                maxInvalidCodeAttempts,
                allowAuthorizedPerson,
                reminderAfterDays,
                allowStorageExtension,
                storageExtensionDays
        );
    }
}
