package com.warehouse.mcp.application.port.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.McpFixture;
import com.warehouse.mcp.application.port.primary.result.DeviceList;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.application.port.secondary.DeviceServicePort;
import com.warehouse.mcp.application.port.secondary.ProcessServicePort;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.service.PersonalDataMasking;
import com.warehouse.mcp.domain.vo.DeviceAccess;
import com.warehouse.mcp.domain.vo.DeviceFilter;

class McpQueryPortImplTest {

    private final CurrentUserServicePort currentUserServicePort = mock(CurrentUserServicePort.class);
    private final ShipmentServicePort shipmentServicePort = mock(ShipmentServicePort.class);
    private final ProcessServicePort processServicePort = mock(ProcessServicePort.class);
    private final DeviceServicePort deviceServicePort = mock(DeviceServicePort.class);
    private final McpQueryPortImpl port = new McpQueryPortImpl(currentUserServicePort, shipmentServicePort,
            processServicePort, deviceServicePort, new PersonalDataMasking());

    @Test
    void shouldMaskPartiesAndLimitShipmentEvents() {
        final TrackingNumber trackingNumber = new TrackingNumber("TRK-001");
        when(shipmentServicePort.findShipmentTracking(trackingNumber)).thenReturn(McpFixture.shipmentTracking(15));

        final ShipmentOverview shipment = port.findShipment(trackingNumber);

        assertThat(shipment.recipient().initials()).isEqualTo("J. N.");
        assertThat(shipment.totalEvents()).isEqualTo(15);
        assertThat(shipment.latestEvents()).hasSize(McpQueryPortImpl.MAX_SHIPMENT_EVENTS);
        assertThat(shipment.latestEvents().getFirst().description()).isEqualTo("Event 0");
    }

    @Test
    void shouldRejectDeviceListForRegularUser() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("USER"));

        assertThatThrownBy(() -> port.listDevices(new DeviceFilter(null, null)))
                .isInstanceOf(McpToolAccessException.class);
        verifyNoInteractions(deviceServicePort);
    }

    @Test
    void shouldFilterAndLimitDevicesForManager() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("MANAGER"));
        final List<DeviceAccess> devices = IntStream.range(0, 60)
                .mapToObj(index -> McpFixture.device("MB:" + index, index % 2 == 0 ? "ACTIVE" : "BLOCKED",
                        index < 55 ? "KT1" : "KT2"))
                .toList();
        when(deviceServicePort.findAllDevices()).thenReturn(devices);

        final DeviceList all = port.listDevices(new DeviceFilter(null, null));
        final DeviceList active = port.listDevices(new DeviceFilter("active", "kt1"));

        assertThat(all.totalDevices()).isEqualTo(60);
        assertThat(all.devices()).hasSize(McpQueryPortImpl.MAX_DEVICES);
        assertThat(active.totalDevices()).isEqualTo(28);
        assertThat(active.devices()).allSatisfy(device -> {
            assertThat(device.status()).isEqualTo("ACTIVE");
            assertThat(device.departmentCode()).isEqualTo("KT1");
        });
    }
}
