package com.warehouse.mcp.configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.auth.McpToolAccessApiService;
import com.warehouse.deliverynetwork.api.DeliveryNetworkApiService;
import com.warehouse.logisticsdelivery.api.DeliveryManagementApiService;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.shipment.infrastructure.ShipmentManagementApiService;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.terminal.DeviceApiService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.mcp.infrastructure.adapter.primary.McpToolVisibilityTransport;
import io.modelcontextprotocol.server.McpStatelessSyncServer;
import io.modelcontextprotocol.spec.McpStatelessServerTransport;
import org.junit.jupiter.api.Test;
import org.springframework.ai.mcp.server.autoconfigure.McpServerStatelessWebMvcAutoConfiguration;
import org.springframework.ai.mcp.server.common.autoconfigure.McpServerStatelessAutoConfiguration;
import org.springframework.ai.mcp.server.common.autoconfigure.StatelessToolCallbackConverterAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfigurations;
import org.springframework.boot.test.context.runner.WebApplicationContextRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;

class McpTransportConfigurationTest {

    private final WebApplicationContextRunner contextRunner = new WebApplicationContextRunner()
            .withUserConfiguration(McpServerConfiguration.class)
            .withConfiguration(AutoConfigurations.of(McpServerStatelessWebMvcAutoConfiguration.class,
                    StatelessToolCallbackConverterAutoConfiguration.class, McpServerStatelessAutoConfiguration.class))
            .withPropertyValues("spring.ai.mcp.server.protocol=STATELESS")
            .withBean("mcpServerObjectMapper", ObjectMapper.class, ObjectMapper::new)
            .withBean(CurrentUserApiService.class, () -> mock(CurrentUserApiService.class))
            .withBean(McpToolAccessApiService.class, () -> mock(McpToolAccessApiService.class))
            .withBean(DeliveryNetworkApiService.class, () -> mock(DeliveryNetworkApiService.class))
            .withBean(DeliveryManagementApiService.class, () -> mock(DeliveryManagementApiService.class))
            .withBean(OperatorConfigurationApiService.class, () -> mock(OperatorConfigurationApiService.class))
            .withBean(DepartmentApiService.class, () -> mock(DepartmentApiService.class))
            .withBean(ProcessHubApiService.class, () -> mock(ProcessHubApiService.class))
            .withBean(ShipmentApiService.class, () -> mock(ShipmentApiService.class))
            .withBean(ShipmentManagementApiService.class, () -> mock(ShipmentManagementApiService.class))
            .withBean(SupplierApiService.class, () -> mock(SupplierApiService.class))
            .withBean(DeviceApiService.class, () -> mock(DeviceApiService.class));

    @Test
    void shouldUseVisibilityTransportWithSpringAiAutoconfiguration() {
        contextRunner.run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).hasSingleBean(McpStatelessSyncServer.class);
            assertThat(context.getBean(McpStatelessServerTransport.class)).isInstanceOf(McpToolVisibilityTransport.class);
        });
    }

    @Test
    void shouldStartWhenMcpIsDisabled() {
        contextRunner.withPropertyValues("spring.ai.mcp.server.enabled=false").run(context -> {
            assertThat(context).hasNotFailed();
            assertThat(context).doesNotHaveBean(McpStatelessServerTransport.class);
        });
    }
}
