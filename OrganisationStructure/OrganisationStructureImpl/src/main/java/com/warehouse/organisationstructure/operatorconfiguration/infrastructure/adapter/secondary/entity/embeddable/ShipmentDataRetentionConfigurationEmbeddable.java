package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.embeddable;

import com.warehouse.organisationstructure.operatorconfiguration.domain.model.ShipmentDataRetentionConfiguration;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class ShipmentDataRetentionConfigurationEmbeddable {

    @Column(name = "shipment_anonymize_recipient_data_after_days")
    private int anonymizeRecipientDataAfterDays;

    @Column(name = "shipment_archive_closed_shipments_after_days")
    private int archiveClosedShipmentsAfterDays;

    @Column(name = "shipment_delivery_photo_retention_days")
    private int deliveryPhotoRetentionDays;

    public ShipmentDataRetentionConfigurationEmbeddable() {
    }

    public static ShipmentDataRetentionConfigurationEmbeddable from(final ShipmentDataRetentionConfiguration configuration) {
        final ShipmentDataRetentionConfiguration source = configuration != null
                ? configuration
                : ShipmentDataRetentionConfiguration.defaultConfiguration();
        final ShipmentDataRetentionConfigurationEmbeddable embeddable = new ShipmentDataRetentionConfigurationEmbeddable();
        embeddable.anonymizeRecipientDataAfterDays = source.getAnonymizeRecipientDataAfterDays();
        embeddable.archiveClosedShipmentsAfterDays = source.getArchiveClosedShipmentsAfterDays();
        embeddable.deliveryPhotoRetentionDays = source.getDeliveryPhotoRetentionDays();
        return embeddable;
    }

    public ShipmentDataRetentionConfiguration toModel() {
        return new ShipmentDataRetentionConfiguration(
                anonymizeRecipientDataAfterDays,
                archiveClosedShipmentsAfterDays,
                deliveryPhotoRetentionDays
        );
    }
}
