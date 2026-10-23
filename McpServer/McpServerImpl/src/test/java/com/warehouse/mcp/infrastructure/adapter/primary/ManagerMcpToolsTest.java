package com.warehouse.mcp.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.McpFixture;
import com.warehouse.mcp.application.port.primary.McpQueryPort;
import com.warehouse.mcp.application.port.primary.result.ShipmentOverview;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.service.PersonalDataMasking;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CurrentUserToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentToolResponse;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ManagerMcpToolsTest {

    private final McpQueryPort mcpQueryPort = mock(McpQueryPort.class);
    private final ManagerMcpTools tools = new ManagerMcpTools(mcpQueryPort, new McpToolResponseMapper());

    @Test
    void shouldReturnCurrentUser() {
        when(mcpQueryPort.currentUser()).thenReturn(McpFixture.user("MANAGER"));

        final CurrentUserToolResponse user = tools.whoami();

        assertThat(user.username()).isEqualTo("anna.kowalska");
        assertThat(user.fullName()).isEqualTo("Anna Kowalska");
        assertThat(user.operatorId()).isEqualTo(20L);
    }

    @Test
    void shouldReturnShipmentWithMaskedRecipientAndIsoDates() {
        final PersonalDataMasking masking = new PersonalDataMasking();
        when(mcpQueryPort.findShipment(new TrackingNumber("TRK-001"))).thenReturn(new ShipmentOverview(
                McpFixture.shipmentTracking(1).shipmentId(), "TRK-001", McpFixture.shipmentTracking(1).status(),
                McpFixture.shipmentTracking(1).createdAt(), null, null, masking.mask(McpFixture.recipient()), 1,
                McpFixture.shipmentTracking(1).events()));

        final ShipmentToolResponse shipment = tools.findShipment(" TRK-001 ");

        assertThat(shipment.shipmentId()).isEqualTo(1L);
        assertThat(shipment.createdAt()).isEqualTo("2026-10-20T07:00");
        assertThat(shipment.recipient().initials()).isEqualTo("J. N.");
        assertThat(shipment.latestEvents().getFirst().processId()).isEqualTo(McpFixture.PROCESS_ID.value().toString());
    }

    @Test
    void shouldPassAccessDeniedMessage() {
        when(mcpQueryPort.listDevices(any())).thenThrow(new McpToolAccessException("Listing devices requires the MANAGER role or higher"));

        assertThatThrownBy(() -> tools.listDevices(null, null))
                .isInstanceOf(McpToolFailure.class)
                .hasMessage("Listing devices requires the MANAGER role or higher");
    }

    @Test
    void shouldRejectInvalidProcessIdWithoutCallingPort() {
        assertThatThrownBy(() -> tools.getProcess("not-a-uuid"))
                .isInstanceOf(McpToolFailure.class)
                .hasMessage("Parameter processId must be a UUID");
        verifyNoInteractions(mcpQueryPort);
    }

    @Test
    void shouldRequireTrackingNumber() {
        assertThatThrownBy(() -> tools.findShipment(" "))
                .isInstanceOf(McpToolFailure.class)
                .hasMessage("Parameter trackingNumber is required");
    }
}
