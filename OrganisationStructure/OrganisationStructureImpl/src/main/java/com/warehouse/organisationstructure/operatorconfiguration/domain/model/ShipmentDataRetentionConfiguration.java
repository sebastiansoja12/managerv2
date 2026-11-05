package com.warehouse.organisationstructure.operatorconfiguration.domain.model;

public class ShipmentDataRetentionConfiguration {
    private int anonymizeRecipientDataAfterDays;
    private int archiveClosedShipmentsAfterDays;
    private int deliveryPhotoRetentionDays;

    public ShipmentDataRetentionConfiguration() {
    }

    public ShipmentDataRetentionConfiguration(final int anonymizeRecipientDataAfterDays,
                                              final int archiveClosedShipmentsAfterDays,
                                              final int deliveryPhotoRetentionDays) {
        this.anonymizeRecipientDataAfterDays = anonymizeRecipientDataAfterDays;
        this.archiveClosedShipmentsAfterDays = archiveClosedShipmentsAfterDays;
        this.deliveryPhotoRetentionDays = deliveryPhotoRetentionDays;
    }

    public static ShipmentDataRetentionConfiguration defaultConfiguration() {
        return new ShipmentDataRetentionConfiguration(365, 90, 180);
    }

    public int getAnonymizeRecipientDataAfterDays() { return anonymizeRecipientDataAfterDays; }
    public int getArchiveClosedShipmentsAfterDays() { return archiveClosedShipmentsAfterDays; }
    public int getDeliveryPhotoRetentionDays() { return deliveryPhotoRetentionDays; }
}
