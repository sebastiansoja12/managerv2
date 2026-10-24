package com.warehouse.mcp;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.mcp.domain.vo.CurrentUserProfile;
import com.warehouse.mcp.domain.vo.DeviceAccess;
import com.warehouse.mcp.domain.vo.ShipmentEvent;
import com.warehouse.mcp.domain.vo.ShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentTracking;

public final class McpFixture {

    public static final ProcessId PROCESS_ID = new ProcessId(UUID.fromString("6f0f3a5e-2d0c-4b8e-9a39-2f7d0d3c1a11"));

    private McpFixture() {
    }

    public static CurrentUserProfile user(final String role) {
        return new CurrentUserProfile(new UserId(5L), "anna.kowalska", "Anna", "Kowalska", role, "KT1",
                OperatorId.of(20L));
    }

    public static ShipmentParty recipient() {
        return new ShipmentParty("Jan", "Nowak", "jan.nowak@example.com", "+48 600 123 789", "Katowice", "40-001",
                "PL");
    }

    public static ShipmentTracking shipmentTracking(final int eventCount) {
        final List<ShipmentEvent> events = IntStream.range(0, eventCount)
                .mapToObj(index -> new ShipmentEvent(LocalDateTime.of(2026, 10, 20, 8, 0).plusHours(eventCount - index),
                        ShipmentStatus.SENT, ProcessType.ROUTE, PROCESS_ID, "KT1", "courier", "Event " + index))
                .toList();
        return new ShipmentTracking(new ShipmentId(1L), "TRK-001", ShipmentStatus.SENT,
                LocalDateTime.of(2026, 10, 20, 7, 0), LocalDateTime.of(2026, 10, 21, 9, 0),
                recipient(), recipient(), events);
    }

    public static DeviceAccess device(final String deviceId, final String status, final String departmentCode) {
        return new DeviceAccess(deviceId, "MOBILE", status, departmentCode, "SHARED", 101L, "1.0.0", true,
                Instant.parse("2026-10-23T08:00:00Z"), 202L, Instant.parse("2026-10-23T20:00:00Z"));
    }
}
