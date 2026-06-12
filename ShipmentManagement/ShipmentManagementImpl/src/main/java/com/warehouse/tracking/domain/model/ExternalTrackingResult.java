package com.warehouse.tracking.domain.model;

import java.time.OffsetDateTime;
import java.util.List;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;

public record ExternalTrackingResult(TrackingProviderId provider,
                                     String trackingNumber,
                                     String currentStatus,
                                     OffsetDateTime updatedAt,
                                     List<TrackingEvent> events,
                                     TrackingLocation origin,
                                     TrackingLocation destination,
                                     String shipmentType,
                                     String deliveryRecipientName,
                                     String deliveryNotes,
                                     String returnTrackingNumber) {
}
