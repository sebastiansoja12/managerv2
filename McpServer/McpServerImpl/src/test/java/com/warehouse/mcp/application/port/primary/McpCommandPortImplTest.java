package com.warehouse.mcp.application.port.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.McpFixture;
import com.warehouse.mcp.application.port.secondary.ConfigurationServicePort;
import com.warehouse.mcp.application.port.secondary.CourierServicePort;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.application.port.secondary.DeliveryServicePort;
import com.warehouse.mcp.application.port.secondary.DepartmentServicePort;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.exception.McpInvalidRequestException;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.service.ConfigurationMerge;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.domain.vo.Courier;
import com.warehouse.mcp.domain.vo.CourierChange;
import com.warehouse.mcp.domain.vo.DeliveryConditions;
import com.warehouse.mcp.domain.vo.DeliveryDetails;
import com.warehouse.mcp.domain.vo.DeliveryRoute;
import com.warehouse.mcp.domain.vo.DeliveryRouteChange;
import com.warehouse.mcp.domain.vo.PartyRole;
import com.warehouse.mcp.domain.vo.ShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentPartyChange;

class McpCommandPortImplTest {

    private static final DeliveryId DELIVERY_ID = new DeliveryId("d-1");

    private final CurrentUserServicePort currentUserServicePort = mock(CurrentUserServicePort.class);
    private final ShipmentServicePort shipmentServicePort = mock(ShipmentServicePort.class);
    private final DepartmentServicePort departmentServicePort = mock(DepartmentServicePort.class);
    private final DeliveryServicePort deliveryServicePort = mock(DeliveryServicePort.class);
    private final CourierServicePort courierServicePort = mock(CourierServicePort.class);
    private final ConfigurationServicePort configurationServicePort = mock(ConfigurationServicePort.class);
    private final McpQueryPort mcpQueryPort = mock(McpQueryPort.class);

    private McpCommandPortImpl port(final boolean writeEnabled) {
        return new McpCommandPortImpl(currentUserServicePort, shipmentServicePort, departmentServicePort,
                deliveryServicePort, courierServicePort, configurationServicePort, mcpQueryPort,
                new ConfigurationMerge(), writeEnabled);
    }

    @Test
    void shouldRejectChangesWhenWritingIsDisabled() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("SUPERADMIN"));

        assertThatThrownBy(() -> port(false).cancelDelivery(DELIVERY_ID))
                .isInstanceOf(McpToolAccessException.class)
                .hasMessageContaining("MCP_WRITE_ENABLED");
        verifyNoInteractions(deliveryServicePort);
    }

    @Test
    void shouldRejectChangesForRegularUser() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("USER"));

        assertThatThrownBy(() -> port(true).cancelDelivery(DELIVERY_ID))
                .isInstanceOf(McpToolAccessException.class)
                .hasMessage("Changing data requires the MANAGER role or higher");
        verifyNoInteractions(deliveryServicePort);
    }

    @Test
    void shouldRequireAdministratorToChangeDepartmentStatus() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("MANAGER"));

        assertThatThrownBy(() -> port(true).changeDepartmentStatus(new DepartmentCode("KT1"), "SUSPENDED"))
                .isInstanceOf(McpToolAccessException.class);
        verifyNoInteractions(departmentServicePort);
    }

    @Test
    void shouldKeepCurrentRouteValuesThatWereNotGiven() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("MANAGER"));
        when(departmentServicePort.findAllDepartments()).thenReturn(List.of(McpFixture.department("PO1", "BRANCH",
                "ACTIVE")));
        final DeliveryDetails current = delivery(new DepartmentId(1L), new DepartmentId(2L));
        when(deliveryServicePort.findDelivery(DELIVERY_ID)).thenReturn(current);
        when(deliveryServicePort.changeRoute(eq(DELIVERY_ID), any())).thenReturn(current);

        port(true).changeDeliveryRoute(DELIVERY_ID, new DeliveryRouteChange(null, new DepartmentCode("PO1"), null,
                null, null));

        final ArgumentCaptor<DeliveryRoute> route = ArgumentCaptor.forClass(DeliveryRoute.class);
        verify(deliveryServicePort).changeRoute(eq(DELIVERY_ID), route.capture());
        assertThat(route.getValue().originDepartmentId()).isEqualTo(new DepartmentId(1L));
        assertThat(route.getValue().destinationDepartmentId())
                .isEqualTo(new DepartmentId((long) "PO1".hashCode()));
        assertThat(route.getValue().pickupMethod()).isEqualTo("DEPARTMENT");
    }

    @Test
    void shouldRejectUnknownDepartmentInRoute() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("MANAGER"));
        when(deliveryServicePort.findDelivery(DELIVERY_ID)).thenReturn(delivery(null, null));

        assertThatThrownBy(() -> port(true).changeDeliveryRoute(DELIVERY_ID,
                new DeliveryRouteChange(new DepartmentCode("XX9"), null, null, null, null)))
                .isInstanceOf(McpInvalidRequestException.class)
                .hasMessage("Unknown department XX9");
    }

    @Test
    void shouldKeepCurrentConditionsThatWereNotGiven() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("MANAGER"));
        final DeliveryDetails current = delivery(null, null);
        when(deliveryServicePort.findDelivery(DELIVERY_ID)).thenReturn(current);
        when(deliveryServicePort.changeConditions(eq(DELIVERY_ID), any())).thenReturn(current);

        port(true).changeDeliveryConditions(DELIVERY_ID, new DeliveryConditions(null, null, "Leave at reception",
                null));

        verify(deliveryServicePort).changeConditions(DELIVERY_ID, new DeliveryConditions("COURIER", true,
                "Leave at reception", LocalDateTime.of(2026, 11, 5, 10, 0)));
    }

    @Test
    void shouldChangeOnlyGivenPartyFields() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("MANAGER"));
        final TrackingNumber trackingNumber = new TrackingNumber("TRK-001");
        when(shipmentServicePort.findShipmentTracking(trackingNumber)).thenReturn(McpFixture.shipmentTracking(1));

        port(true).changeShipmentParty(trackingNumber, PartyRole.RECIPIENT,
                new ShipmentPartyChange(null, null, null, "600100200", null, null, null, null));

        verify(shipmentServicePort).changeParty(new ShipmentId(1L), PartyRole.RECIPIENT, new ShipmentParty("Jan",
                "Nowak", "jan.nowak@example.com", "600100200", "Katowice", "40-001", "Mariacka 1", "PL"));
        verify(mcpQueryPort).findShipment(trackingNumber);
    }

    @Test
    void shouldMergeCourierChangeWithCurrentData() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("MANAGER"));
        final SupplierCode courierCode = new SupplierCode("SUP-1");
        when(courierServicePort.findCouriers()).thenReturn(List.of(new Courier(null, courierCode, "Adam", "Kurier",
                "600000000", new DepartmentCode("KT1"), "ACTIVE")));

        port(true).updateCourier(new CourierChange(courierCode, null, "Nowak", null));

        verify(courierServicePort).update(new CourierChange(courierCode, "Adam", "Nowak", "600000000"));
    }

    @Test
    void shouldUpdateConfigurationWithMergedSection() {
        when(currentUserServicePort.currentUser()).thenReturn(McpFixture.user("ADMIN"));
        when(configurationServicePort.find(ConfigurationSection.FLEET))
                .thenReturn(Map.of("maxVehicles", 10, "electricOnly", false));
        when(configurationServicePort.update(eq(ConfigurationSection.FLEET), any()))
                .thenReturn(Map.of("maxVehicles", 20, "electricOnly", false));

        port(true).updateConfiguration(ConfigurationSection.FLEET, Map.of("maxVehicles", 20));

        verify(configurationServicePort).update(ConfigurationSection.FLEET,
                Map.of("maxVehicles", 20, "electricOnly", false));
    }

    private DeliveryDetails delivery(final DepartmentId origin, final DepartmentId destination) {
        return new DeliveryDetails(DELIVERY_ID, new ShipmentId(1L), "SHIPMENT", null, "PLANNED", origin, destination,
                "DEPARTMENT", "COURIER", null, null, null, null, true, "Call first", LocalDateTime.of(2026, 11, 1, 8, 0),
                LocalDateTime.of(2026, 11, 5, 10, 0), null, List.of());
    }
}
