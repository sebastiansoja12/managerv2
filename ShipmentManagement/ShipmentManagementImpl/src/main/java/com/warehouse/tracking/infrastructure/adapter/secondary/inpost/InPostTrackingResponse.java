package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import java.time.OffsetDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
record InPostTrackingResponse(List<Parcel> parcels) {

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Parcel(List<Event> events,
                  Location origin,
                  Location destination,
                  Delivery delivery,
                  Shipment shipment,
                  String trackingNumber,
                  String status,
                  ReturnToSender returnToSender) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Event(OffsetDateTime eventTimestamp,
                 String eventCode,
                 String status,
                 String eventId,
                 Location location) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Location(String id,
                    String address,
                    String city,
                    String country,
                    String name,
                    String postalCode,
                    String type,
                    String description) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Delivery(String recipientName, String deliveryNotes) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record Shipment(String type) {
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    record ReturnToSender(String trackingNumber) {
    }
}
