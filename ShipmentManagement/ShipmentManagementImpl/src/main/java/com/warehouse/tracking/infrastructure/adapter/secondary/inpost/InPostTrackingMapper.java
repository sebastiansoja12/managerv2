package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.ExternalTrackingResult;
import com.warehouse.tracking.domain.model.TrackingEvent;
import com.warehouse.tracking.domain.model.TrackingLocation;

final class InPostTrackingMapper {

    private InPostTrackingMapper() {
    }

    static ExternalTrackingResult toModel(final InPostTrackingResponse.Parcel parcel) {
        final List<TrackingEvent> events = parcel.events() == null ? List.of() : parcel.events().stream()
                .sorted(Comparator.comparing(InPostTrackingResponse.Event::eventTimestamp,
                        Comparator.nullsLast(Comparator.reverseOrder())))
                .map(InPostTrackingMapper::toEvent)
                .toList();
        final InPostTrackingResponse.Event latestEvent = parcel.events() == null ? null : parcel.events().stream()
                .filter(event -> event.eventTimestamp() != null)
                .max(Comparator.comparing(InPostTrackingResponse.Event::eventTimestamp))
                .orElse(null);
        final String currentStatus = firstNonBlank(parcel.status(),
                latestEvent != null ? latestEvent.status() : null,
                latestEvent != null ? latestEvent.eventCode() : null);
        final OffsetDateTime updatedAt = latestEvent != null ? latestEvent.eventTimestamp() : null;

        return new ExternalTrackingResult(
                TrackingProviderId.INPOST,
                parcel.trackingNumber(),
                currentStatus,
                updatedAt,
                events,
                toLocation(parcel.origin()),
                toLocation(parcel.destination()),
                parcel.shipment() != null ? parcel.shipment().type() : null,
                parcel.delivery() != null ? parcel.delivery().recipientName() : null,
                parcel.delivery() != null ? parcel.delivery().deliveryNotes() : null,
                parcel.returnToSender() != null ? parcel.returnToSender().trackingNumber() : null);
    }

    private static TrackingEvent toEvent(final InPostTrackingResponse.Event event) {
        final String name = firstNonBlank(event.status(), event.eventCode());
        return new TrackingEvent(event.eventTimestamp(), name, null, event.eventCode(), toLocation(event.location()));
    }

    private static TrackingLocation toLocation(final InPostTrackingResponse.Location location) {
        if (location == null) {
            return null;
        }
        return new TrackingLocation(location.id(), location.type(), location.name(), location.address(),
                location.postalCode(), location.city(), location.country(), location.description());
    }

    private static String firstNonBlank(final String... values) {
        for (final String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
