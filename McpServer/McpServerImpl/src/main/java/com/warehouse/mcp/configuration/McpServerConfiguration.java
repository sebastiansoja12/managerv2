package com.warehouse.mcp.configuration;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.auth.CurrentUserApiService;
import com.warehouse.deliverynetwork.api.DeliveryNetworkApiService;
import com.warehouse.mcp.application.port.primary.McpQueryPort;
import com.warehouse.mcp.application.port.primary.McpQueryPortImpl;
import com.warehouse.mcp.application.port.secondary.CurrentUserServicePort;
import com.warehouse.mcp.application.port.secondary.DeliveryNetworkServicePort;
import com.warehouse.mcp.application.port.secondary.DepartmentServicePort;
import com.warehouse.mcp.application.port.secondary.DeviceServicePort;
import com.warehouse.mcp.application.port.secondary.ProcessServicePort;
import com.warehouse.mcp.application.port.secondary.ShipmentServicePort;
import com.warehouse.mcp.domain.service.PersonalDataMasking;
import com.warehouse.mcp.infrastructure.adapter.primary.ManagerMcpTools;
import com.warehouse.mcp.infrastructure.adapter.primary.McpToolResponseMapper;
import com.warehouse.mcp.infrastructure.adapter.secondary.CurrentUserServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.DeliveryNetworkServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.DepartmentServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.DeviceServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.ProcessServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.ShipmentServiceAdapter;
import com.warehouse.organisationstructure.department.api.DepartmentApiService;
import com.warehouse.process.ProcessHubApiService;
import com.warehouse.shipment.infrastructure.ShipmentApiService;
import com.warehouse.terminal.DeviceApiService;
import org.springframework.beans.factory.annotation.Value;
import com.warehouse.logisticsdelivery.api.DeliveryManagementApiService;
import com.warehouse.mcp.application.port.primary.McpCommandPort;
import com.warehouse.mcp.application.port.primary.McpCommandPortImpl;
import com.warehouse.mcp.application.port.secondary.ConfigurationServicePort;
import com.warehouse.mcp.application.port.secondary.CourierServicePort;
import com.warehouse.mcp.application.port.secondary.DeliveryServicePort;
import com.warehouse.mcp.domain.service.ConfigurationMerge;
import com.warehouse.mcp.infrastructure.adapter.primary.ManagerMcpCommandTools;
import com.warehouse.mcp.infrastructure.adapter.secondary.ConfigurationServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.CourierServiceAdapter;
import com.warehouse.mcp.infrastructure.adapter.secondary.DeliveryServiceAdapter;
import com.warehouse.organisationstructure.api.OperatorConfigurationApiService;
import com.warehouse.shipment.infrastructure.ShipmentManagementApiService;
import com.warehouse.supplier.api.SupplierApiService;
import com.warehouse.mcp.infrastructure.adapter.primary.McpToolArguments;
import com.warehouse.mcp.infrastructure.adapter.primary.McpToolExecutor;

@Configuration
public class McpServerConfiguration {

    @Bean
    public McpQueryPort mcpQueryPort(final CurrentUserServicePort mcpCurrentUserServicePort,
                                     final ShipmentServicePort mcpShipmentServicePort,
                                     final ProcessServicePort mcpProcessServicePort,
                                     final DeviceServicePort mcpDeviceServicePort,
                                     final DepartmentServicePort mcpDepartmentServicePort,
                                     final DeliveryNetworkServicePort mcpDeliveryNetworkServicePort,
                                     final DeliveryServicePort mcpDeliveryServicePort,
                                     final CourierServicePort mcpCourierServicePort,
                                     final ConfigurationServicePort mcpConfigurationServicePort) {
        return new McpQueryPortImpl(mcpCurrentUserServicePort, mcpShipmentServicePort, mcpProcessServicePort,
                mcpDeviceServicePort, mcpDepartmentServicePort, mcpDeliveryNetworkServicePort, mcpDeliveryServicePort,
                mcpCourierServicePort, mcpConfigurationServicePort, new PersonalDataMasking());
    }

    @Bean
    public McpCommandPort mcpCommandPort(final CurrentUserServicePort mcpCurrentUserServicePort,
                                         final ShipmentServicePort mcpShipmentServicePort,
                                         final DepartmentServicePort mcpDepartmentServicePort,
                                         final DeliveryServicePort mcpDeliveryServicePort,
                                         final CourierServicePort mcpCourierServicePort,
                                         final ConfigurationServicePort mcpConfigurationServicePort,
                                         final McpQueryPort mcpQueryPort,
                                         @Value("${mcp.server.write-enabled:true}") final boolean writeEnabled) {
        return new McpCommandPortImpl(mcpCurrentUserServicePort, mcpShipmentServicePort, mcpDepartmentServicePort,
                mcpDeliveryServicePort, mcpCourierServicePort, mcpConfigurationServicePort, mcpQueryPort,
                new ConfigurationMerge(), writeEnabled);
    }

    @Bean
    public DeliveryServicePort mcpDeliveryServicePort(
            final DeliveryManagementApiService deliveryManagementApiService) {
        return new DeliveryServiceAdapter(deliveryManagementApiService);
    }

    @Bean
    public CourierServicePort mcpCourierServicePort(final SupplierApiService supplierApiService) {
        return new CourierServiceAdapter(supplierApiService);
    }

    @Bean
    public ConfigurationServicePort mcpConfigurationServicePort(
            final OperatorConfigurationApiService operatorConfigurationApiService) {
        return new ConfigurationServiceAdapter(operatorConfigurationApiService);
    }

    @Bean
    public CurrentUserServicePort mcpCurrentUserServicePort(final CurrentUserApiService currentUserApiService) {
        return new CurrentUserServiceAdapter(currentUserApiService);
    }

    @Bean
    public ShipmentServicePort mcpShipmentServicePort(final ShipmentApiService shipmentApiService,
                                                      final ShipmentManagementApiService shipmentManagementApiService) {
        return new ShipmentServiceAdapter(shipmentApiService, shipmentManagementApiService);
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
    public DepartmentServicePort mcpDepartmentServicePort(final DepartmentApiService departmentApiService) {
        return new DepartmentServiceAdapter(departmentApiService);
    }

    @Bean
    public DeliveryNetworkServicePort mcpDeliveryNetworkServicePort(
            final DeliveryNetworkApiService deliveryNetworkApiService) {
        return new DeliveryNetworkServiceAdapter(deliveryNetworkApiService);
    }

    @Bean
    public McpToolExecutor mcpToolExecutor() {
        return new McpToolExecutor();
    }

    @Bean
    public McpToolArguments mcpToolArguments() {
        return new McpToolArguments();
    }

    @Bean
    public ManagerMcpTools managerMcpTools(final McpQueryPort mcpQueryPort,
                                           final McpToolExecutor mcpToolExecutor,
                                           final McpToolArguments mcpToolArguments) {
        return new ManagerMcpTools(mcpQueryPort, new McpToolResponseMapper(), mcpToolExecutor, mcpToolArguments);
    }

    @Bean
    public ManagerMcpCommandTools managerMcpCommandTools(final McpCommandPort mcpCommandPort,
                                                         final McpToolExecutor mcpToolExecutor,
                                                         final McpToolArguments mcpToolArguments) {
        return new ManagerMcpCommandTools(mcpCommandPort, new McpToolResponseMapper(), mcpToolExecutor,
                mcpToolArguments);
    }

    @Bean
    public ToolCallbackProvider managerMcpToolCallbackProvider(final ManagerMcpTools managerMcpTools,
                                                               final ManagerMcpCommandTools managerMcpCommandTools) {
        return MethodToolCallbackProvider.builder().toolObjects(managerMcpTools, managerMcpCommandTools).build();
    }
}
