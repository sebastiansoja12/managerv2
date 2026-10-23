package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.model.device.Mobile;
import com.warehouse.terminal.domain.vo.DeviceHardware;
import com.warehouse.terminal.domain.vo.DeviceIdentity;
import com.warehouse.terminal.domain.vo.DeviceLocation;
import com.warehouse.terminal.domain.vo.DeviceNetwork;
import com.warehouse.terminal.domain.vo.DeviceSoftware;

class OwnershipReassignmentTest {

    private static final Instant ASSIGNED_AT = Instant.parse("2026-10-23T12:00:00Z");

    @Test
    void shouldKeepPreviousOwnerWhenDeviceIsReassigned() {
        final OwnershipProfile ownership = ownership();

        ownership.reassignTo(DeviceAccessFixture.OTHER_USER_ID, ASSIGNED_AT);

        assertEquals(DeviceAccessFixture.OTHER_USER_ID, ownership.getUserId());
        assertEquals(DeviceAccessFixture.OWNER_ID, ownership.getPreviousUserId());
        assertEquals(ASSIGNED_AT, ownership.getAssignedAt());
        assertNull(ownership.getUnassignedAt());
    }

    @Test
    void shouldNotOverwritePreviousOwnerWhenSameUserIsAssignedAgain() {
        final OwnershipProfile ownership = ownership();
        ownership.reassignTo(DeviceAccessFixture.OTHER_USER_ID, ASSIGNED_AT);

        ownership.reassignTo(DeviceAccessFixture.OTHER_USER_ID, ASSIGNED_AT.plusSeconds(60));

        assertEquals(DeviceAccessFixture.OWNER_ID, ownership.getPreviousUserId());
        assertEquals(ASSIGNED_AT, ownership.getAssignedAt());
    }

    @Test
    void shouldMovePreviousOwnerWhenMobileDeviceIsAssignedToAnotherUser() {
        final Mobile mobile = new Mobile(new DeviceId("MB:00000000-0000-0000-0000-000000000001"), new DeviceIdentity(),
                new DeviceHardware(), new DeviceSoftware(), new DeviceNetwork(), new DeviceLocation(), ownership());

        mobile.assignUser(DeviceAccessFixture.OTHER_USER_ID);

        assertEquals(DeviceAccessFixture.OTHER_USER_ID, mobile.getUserId());
        assertEquals(DeviceAccessFixture.OWNER_ID, mobile.getOwnership().getPreviousUserId());
    }

    private OwnershipProfile ownership() {
        return new OwnershipProfile(Instant.parse("2026-01-01T00:00:00Z"), "COURIER", DeviceAccessFixture.DEPARTMENT_CODE,
                null, null, null, DeviceAccessFixture.OWNER_ID, null, DeviceAccessFixture.OPERATOR_ID,
                DeviceAssignmentMode.PERSONAL);
    }
}
