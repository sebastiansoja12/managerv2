package com.warehouse.logistics.infrastructure.adapter.primary;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import com.warehouse.terminal.jaxb.CollectRequest;
import com.warehouse.terminal.jaxb.DeviceType;
import com.warehouse.terminal.jaxb.PalletScanRequest;
import com.warehouse.terminal.jaxb.TerminalRequest;

class DeviceTypeResolverTest {

    private final DeviceTypeResolver resolver = new DeviceTypeResolver();

    @Test
    void shouldResolveDeviceFromTerminalRequest() {
        final DeviceType device = new DeviceType();
        final TerminalRequest request = new TerminalRequest();
        request.setDevice(device);

        assertSame(device, resolver.resolve(new Object[]{request}));
    }

    @Test
    void shouldResolveDeviceInformationFromCollectRequest() {
        final DeviceType device = new DeviceType();
        final CollectRequest request = new CollectRequest();
        request.setDeviceInformation(device);

        assertSame(device, resolver.resolve(new Object[]{request}));
    }

    @Test
    void shouldResolveDeviceFromAnyArgumentPosition() {
        final DeviceType device = new DeviceType();
        final PalletScanRequest request = new PalletScanRequest();
        request.setDeviceInformation(device);

        assertSame(device, resolver.resolve(new Object[]{"other", null, request}));
    }

    @Test
    void shouldRejectRequestWithoutDeviceData() {
        final Object[] arguments = {new PalletScanRequest()};

        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(arguments));
    }

    @Test
    void shouldRejectArgumentsWithoutDeviceElement() {
        final Object[] arguments = {"not a request"};

        assertThrows(IllegalArgumentException.class, () -> resolver.resolve(arguments));
    }
}
