package com.warehouse.terminal;

import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.mock;

import java.time.Duration;
import java.time.Instant;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.terminal.domain.enumeration.DeviceAssignmentMode;
import com.warehouse.terminal.domain.enumeration.DeviceStatus;
import com.warehouse.terminal.domain.model.Device;
import com.warehouse.terminal.domain.model.DevicePair;
import com.warehouse.terminal.domain.model.OwnershipProfile;
import com.warehouse.terminal.domain.vo.DevicePairId;

final class DeviceAccessFixture {

    static final DeviceId DEVICE_ID = new DeviceId("MB:00000000-0000-0000-0000-000000000001");
    static final UserId OWNER_ID = new UserId(101L);
    static final UserId OTHER_USER_ID = new UserId(202L);
    static final DepartmentCode DEPARTMENT_CODE = new DepartmentCode("KT1");
    static final OperatorId OPERATOR_ID = OperatorId.of(7L);
    static final String PAIR_KEY = "pair-key";

    private DeviceAccessFixture() {
    }

    static Device device(final DeviceStatus status, final DeviceAssignmentMode assignmentMode) {
        final Device device = mock(Device.class);
        final OwnershipProfile ownership = new OwnershipProfile(Instant.now(), "COURIER", DEPARTMENT_CODE, null,
                null, null, OWNER_ID, null, OPERATOR_ID, assignmentMode);
        lenient().when(device.getDeviceId()).thenReturn(DEVICE_ID);
        lenient().when(device.getStatus()).thenReturn(status);
        lenient().when(device.getOwnership()).thenReturn(ownership);
        lenient().when(device.getUserId()).thenReturn(OWNER_ID);
        lenient().when(device.getDepartmentCode()).thenReturn(DEPARTMENT_CODE);
        lenient().when(device.getVersion()).thenReturn("1.0.0");
        return device;
    }

    static DevicePair devicePair() {
        return new DevicePair(DEVICE_ID, PAIR_KEY, Instant.now().plus(Duration.ofDays(1)), new DevicePairId(1L));
    }

    static DevicePair devicePairWithOperator(final UserId userId, final Instant sessionExpiresAt) {
        final DevicePair devicePair = devicePair();
        devicePair.startOperatorSession(userId, sessionExpiresAt);
        return devicePair;
    }
}
