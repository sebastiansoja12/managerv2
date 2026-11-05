package com.warehouse.mcp.infrastructure.adapter.primary;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Map;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.mcp.application.port.primary.McpCommandPort;
import com.warehouse.mcp.application.port.primary.result.ConfigurationView;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ConfigurationToolResponse;

class ManagerMcpCommandToolsTest {

    private final McpCommandPort mcpCommandPort = mock(McpCommandPort.class);
    private final ManagerMcpCommandTools tools = new ManagerMcpCommandTools(mcpCommandPort,
            new McpToolResponseMapper(), new McpToolExecutor(), new McpToolArguments());

    @Test
    void shouldParseConfigurationChangesAsJson() {
        when(mcpCommandPort.updateConfiguration(ConfigurationSection.FLEET, Map.of("maxVehicles", 20)))
                .thenReturn(new ConfigurationView(ConfigurationSection.FLEET, Map.of("maxVehicles", 20)));

        final ConfigurationToolResponse response = tools.updateConfiguration("fleet", "{\"maxVehicles\": 20}");

        assertThat(response.section()).isEqualTo("FLEET");
        assertThat(response.configuration()).containsEntry("maxVehicles", 20);
    }

    @Test
    void shouldRejectInvalidJson() {
        assertThatThrownBy(() -> tools.updateConfiguration("FLEET", "maxVehicles=20"))
                .isInstanceOf(McpToolFailure.class)
                .hasMessage("Parameter changes must be a JSON object");
        verifyNoInteractions(mcpCommandPort);
    }

    @Test
    void shouldRejectUnknownSection() {
        assertThatThrownBy(() -> tools.updateConfiguration("billing", "{}"))
                .isInstanceOf(McpToolFailure.class)
                .hasMessageStartingWith("Unknown configuration section billing");
    }

    @Test
    void shouldPassAccessDeniedMessage() {
        org.mockito.Mockito.doThrow(new McpToolAccessException("Changing data requires the MANAGER role or higher"))
                .when(mcpCommandPort).cancelDelivery(new DeliveryId("d-1"));

        assertThatThrownBy(() -> tools.cancelDelivery("d-1"))
                .isInstanceOf(McpToolFailure.class)
                .hasMessage("Changing data requires the MANAGER role or higher");
    }

    @Test
    void shouldPassValidationMessageFromOtherModules() {
        org.mockito.Mockito.doThrow(new IllegalArgumentException("Telephone number must be exactly 9 digits"))
                .when(mcpCommandPort).cancelDelivery(new DeliveryId("d-1"));

        assertThatThrownBy(() -> tools.cancelDelivery("d-1"))
                .isInstanceOf(McpToolFailure.class)
                .hasMessage("Telephone number must be exactly 9 digits");
    }

    @Test
    void shouldRejectInvalidStepNumberAndDate() {
        assertThatThrownBy(() -> tools.commentDeliveryStep("d-1", 0, "ok"))
                .isInstanceOf(McpToolFailure.class)
                .hasMessage("Parameter stepNumber must be 1 or greater");
        assertThatThrownBy(() -> tools.changeDeliveryConditions("d-1", null, null, null, "tomorrow"))
                .isInstanceOf(McpToolFailure.class)
                .hasMessageStartingWith("Parameter plannedAt must be an ISO-8601");
        verifyNoInteractions(mcpCommandPort);
    }

    @Test
    void shouldCancelDelivery() {
        assertThat(tools.cancelDelivery(" d-1 ").message()).isEqualTo("Delivery d-1 cancelled");
        verify(mcpCommandPort).cancelDelivery(new DeliveryId("d-1"));
    }
}
