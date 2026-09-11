package com.warehouse.terminal;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.warehouse.terminal.domain.model.DevicePair;

class DevicePairOperatorSessionTest {

    @Test
    void shouldReturnActiveOperatorWhenSessionIsValid() {
        final DevicePair devicePair = DeviceAccessFixture.devicePairWithOperator(DeviceAccessFixture.OWNER_ID,
                Instant.now().plus(Duration.ofHours(1)));

        assertEquals(Optional.of(DeviceAccessFixture.OWNER_ID), devicePair.activeOperator(Instant.now()));
    }

    @Test
    void shouldReturnNoOperatorWhenSessionExpired() {
        final DevicePair devicePair = DeviceAccessFixture.devicePairWithOperator(DeviceAccessFixture.OWNER_ID,
                Instant.now().minus(Duration.ofMinutes(1)));

        assertTrue(devicePair.activeOperator(Instant.now()).isEmpty());
    }

    @Test
    void shouldEndOperatorSessionAndReplacePairKeyWhenActivated() {
        final DevicePair devicePair = DeviceAccessFixture.devicePairWithOperator(DeviceAccessFixture.OWNER_ID,
                Instant.now().plus(Duration.ofHours(1)));

        devicePair.activate("new-pair-key", Instant.now().plus(Duration.ofDays(180)));

        assertTrue(devicePair.activeOperator(Instant.now()).isEmpty());
        assertNotEquals(DeviceAccessFixture.PAIR_KEY, devicePair.getPairKey());
        assertTrue(devicePair.isPaired());
    }

    @Test
    void shouldRecognizeSessionOwner() {
        final DevicePair devicePair = DeviceAccessFixture.devicePairWithOperator(DeviceAccessFixture.OWNER_ID,
                Instant.now().plus(Duration.ofHours(1)));

        assertTrue(devicePair.isOperatorSessionOwnedBy(DeviceAccessFixture.OWNER_ID));
        assertFalse(devicePair.isOperatorSessionOwnedBy(DeviceAccessFixture.OTHER_USER_ID));
    }
}
