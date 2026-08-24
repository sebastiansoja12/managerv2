package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentValidationConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentValidationConfigurationEmbeddable {

    @Column(name = "shipment_validate_address_data")
    private boolean validateAddressData;

    @Column(name = "shipment_require_recipient_phone")
    private boolean requireRecipientPhone;

    @Column(name = "shipment_require_recipient_email")
    private boolean requireRecipientEmail;

    @Column(name = "shipment_prevent_duplicate_tracking")
    private boolean preventDuplicateTracking;

    @Column(name = "shipment_require_sender_reference")
    private boolean requireSenderReference;

    @Column(name = "shipment_validate_postal_code")
    private boolean validatePostalCode;

    public ShipmentValidationConfigurationEmbeddable() {
    }

    public static ShipmentValidationConfigurationEmbeddable from(
            final ShipmentValidationConfiguration configuration) {
        final ShipmentValidationConfiguration source = configuration != null
                ? configuration
                : ShipmentValidationConfiguration.defaultConfiguration();
        final ShipmentValidationConfigurationEmbeddable embeddable =
                new ShipmentValidationConfigurationEmbeddable();
        embeddable.validateAddressData = source.isValidateAddressData();
        embeddable.requireRecipientPhone = source.isRequireRecipientPhone();
        embeddable.requireRecipientEmail = source.isRequireRecipientEmail();
        embeddable.preventDuplicateTracking = source.isPreventDuplicateTracking();
        embeddable.requireSenderReference = source.isRequireSenderReference();
        embeddable.validatePostalCode = source.isValidatePostalCode();
        return embeddable;
    }

    public ShipmentValidationConfiguration toModel() {
        return new ShipmentValidationConfiguration(
                validateAddressData,
                requireRecipientPhone,
                requireRecipientEmail,
                preventDuplicateTracking,
                requireSenderReference,
                validatePostalCode
        );
    }
}
