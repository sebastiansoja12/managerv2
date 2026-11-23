package com.warehouse.shipment.api.event;

public final class ShipmentTopics {

    public static final String EVENTS = "shipment.events";
    public static final String CREATED = "shipment.created";
    public static final String CANCELED = "shipment.canceled";
    public static final String DELIVERED = "shipment.delivered";
    public static final String OUT_FOR_DELIVERY = "shipment.out-for-delivery";
    public static final String RETURN_SHIPMENT_DELIVERED = "return.shipment.delivered";
    public static final String READ_MODEL_SYNC = "shipment.read-model.sync";
    public static final String DELIVERY_READ_MODEL_SYNC = "shipment.read-model.delivery-sync";
    public static final String PALLET_READ_MODEL_SYNC = "shipment.read-model.pallet-sync";
    public static final String INCIDENT_EVENTS = "shipment.incident.events";

    private ShipmentTopics() {
    }
}
