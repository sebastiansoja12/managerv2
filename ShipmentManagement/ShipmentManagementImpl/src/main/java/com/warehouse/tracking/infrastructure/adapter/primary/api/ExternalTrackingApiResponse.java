package com.warehouse.tracking.infrastructure.adapter.primary.api;

import java.time.OffsetDateTime;
import java.util.List;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.ExternalTrackingResult;
import com.warehouse.tracking.domain.model.TrackingLocation;

public record ExternalTrackingApiResponse(TrackingProviderId provider,
                                          String trackingNumber,
                                          String currentStatus,
                                          OffsetDateTime updatedAt,
                                          List<Event> events,
                                          Location origin,
                                          Location destination,
                                          String shipmentType,
                                          String deliveryRecipientName,
                                          String deliveryNotes,
                                          String returnTrackingNumber) {

    public static ExternalTrackingApiResponse from(final ExternalTrackingResult result) {
        return new ExternalTrackingApiResponse(result.provider(), result.trackingNumber(), result.currentStatus(),
                result.updatedAt(), result.events().stream()
                .map(event -> new Event(event.timestamp(), event.name(), event.description(),
                        event.eventCode(), Location.from(event.location())))
                .toList(), Location.from(result.origin()), Location.from(result.destination()), result.shipmentType(),
                result.deliveryRecipientName(), result.deliveryNotes(), result.returnTrackingNumber());
    }

    public record Event(OffsetDateTime timestamp,
                        String name,
                        String description,
                        String eventCode,
                        Location location) {
    }

    public record Location(String id,
                           String type,
                           String name,
                           String address,
                           String postalCode,
                           String city,
                           String country,
                           String description) {

        private static Location from(final TrackingLocation location) {
            if (location == null) {
                return null;
            }
            return new Location(location.id(), location.type(), location.name(), location.address(),
                    location.postalCode(), location.city(), location.country(), location.description());
        }
    }
}
