package com.warehouse.tracking.infrastructure.adapter.secondary.inpost;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.OffsetDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.tracking.domain.enumeration.TrackingProviderId;
import com.warehouse.tracking.domain.model.ExternalTrackingResult;

class InPostTrackingMapperTest {

    @Test
    void shouldMapNullableInPostDtoToCommonTrackingModelAndSortEventsNewestFirst() {
        final InPostTrackingResponse.Location location = new InPostTrackingResponse.Location(
                "WAW", "Street 1", "Warsaw", "PL", "Warsaw depot", "00-001", "DEPOT", null);
        final InPostTrackingResponse.Event older = new InPostTrackingResponse.Event(
                OffsetDateTime.parse("2026-08-11T10:00:00Z"), "ACCEPTED", "Accepted", "1", null);
        final InPostTrackingResponse.Event newer = new InPostTrackingResponse.Event(
                OffsetDateTime.parse("2026-08-12T10:00:00Z"), "IN_TRANSIT", "In transit", "2", location);
        final InPostTrackingResponse.Parcel parcel = new InPostTrackingResponse.Parcel(
                List.of(older, newer), null, location, null,
                new InPostTrackingResponse.Shipment("PARCEL"), "TRACK123456", null, null);

        final ExternalTrackingResult result = InPostTrackingMapper.toModel(parcel);

        assertEquals(TrackingProviderId.INPOST, result.provider());
        assertEquals("TRACK123456", result.trackingNumber());
        assertEquals("In transit", result.currentStatus());
        assertEquals(newer.eventTimestamp(), result.updatedAt());
        assertEquals("IN_TRANSIT", result.events().get(0).eventCode());
        assertEquals("Warsaw", result.events().get(0).location().city());
        assertEquals("PARCEL", result.shipmentType());
    }
}
