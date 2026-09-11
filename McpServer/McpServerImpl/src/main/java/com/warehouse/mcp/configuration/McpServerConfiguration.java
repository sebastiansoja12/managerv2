package com.warehouse.mcp.configuration;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.mcp.application.port.primary.McpQueryPort;
import com.warehouse.mcp.application.port.primary.McpQueryPortImpl;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.application.port.secondary.DeviceServicePort;
import com.warehouse.mcp.application.port.secondary.ProcessServicePort;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.service.PersonalDataMasking;
import com.warehouse.mcp.infrastructure.adapter.primary.ManagerMcpTools;
import com.warehouse.mcp.infrastructure.adapter.primary.McpToolResponseMapper;
import com.warehouse.mcp.infrastructure.adapter.secondary.CurrentUserServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.DeviceServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.ProcessServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.terminal.DeviceApiService;

@Configuration
public class McpServerConfiguration {

    @Bean
    public McpQueryPort mcpQueryPort(final CurrentUserServicePort mcpCurrentUserServicePort,
                                     final ShipmentServicePort mcpShipmentServicePort,
                                     final ProcessServicePort mcpProcessServicePort,
                                     final DeviceServicePort mcpDeviceServicePort) {
        return new McpQueryPortImpl(mcpCurrentUserServicePort, mcpShipmentServicePort, mcpProcessServicePort,
                mcpDeviceServicePort, new PersonalDataMasking());
    }

    @Bean
    public CurrentUserServicePort mcpCurrentUserServicePort(final CurrentUserApiService currentUserApiService) {
        return new CurrentUserServiceAdapter(currentUserApiService);
    }

    @Bean
    public ShipmentServicePort mcpShipmentServicePort(final ShipmentApiService shipmentApiService) {
        return new ShipmentServiceAdapter(shipmentApiService);
    }

    @Bean
    public ProcessServicePort mcpProcessServicePort(final ProcessHubApiService processHubApiService) {
        return new ProcessServiceAdapter(processHubApiService);
    }

    @Bean
    public DeviceServicePort mcpDeviceServicePort(final DeviceApiService deviceApiService) {
        return new DeviceServiceAdapter(deviceApiService);
    }

    @Bean
    public ManagerMcpTools managerMcpTools(final McpQueryPort mcpQueryPort) {
        return new ManagerMcpTools(mcpQueryPort, new McpToolResponseMapper());
    }

    @Bean
    public ToolCallbackProvider managerMcpToolCallbackProvider(final ManagerMcpTools managerMcpTools) {
        return MethodToolCallbackProvider.builder().toolObjects(managerMcpTools).build();
    }
}
