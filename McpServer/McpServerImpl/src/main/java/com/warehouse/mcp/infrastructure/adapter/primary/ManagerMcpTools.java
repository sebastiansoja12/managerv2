package com.warehouse.mcp.infrastructure.adapter.primary;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.primary.McpQueryPort;
import com.warehouse.mcp.domain.vo.DepartmentFilter;
import com.warehouse.mcp.domain.vo.DeviceFilter;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CurrentUserToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DepartmentDetailsToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DepartmentListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeviceListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ProcessToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentRouteToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentToolResponse;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.domain.vo.DeliveryFilter;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ConfigurationToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CourierListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeliveryListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeliveryToolResponse;

public class ManagerMcpTools {

    private final McpQueryPort mcpQueryPort;

    private final McpToolResponseMapper responseMapper;

    private final McpToolExecutor executor;

    private final McpToolArguments arguments;

    public ManagerMcpTools(final McpQueryPort mcpQueryPort,
                           final McpToolResponseMapper responseMapper,
                           final McpToolExecutor executor,
                           final McpToolArguments arguments) {
        this.mcpQueryPort = mcpQueryPort;
        this.responseMapper = responseMapper;
        this.executor = executor;
        this.arguments = arguments;
    }

    @Tool(name = "whoami", description = "Returns the Manager account used by this connection: username, role, "
            + "department code and operator. Call it first to learn which department and permissions apply.")
    public CurrentUserToolResponse whoami() {
        return executor.execute("whoami", () -> responseMapper.map(mcpQueryPort.currentUser()));
    }

    @Tool(name = "find_shipment", description = "Finds a shipment by its tracking number and returns its status, "
            + "dates, masked sender and recipient (initials, city, postal code) and the latest tracking events, "
            + "newest first. Only shipments of the caller's operator are visible.")
    public ShipmentToolResponse findShipment(
            @ToolParam(description = "Shipment tracking number exactly as printed on the label")
            final String trackingNumber) {
        return executor.execute("find_shipment",
                () -> responseMapper.map(mcpQueryPort.findShipment(arguments.trackingNumber(trackingNumber))));
    }

    @Tool(name = "get_process", description = "Returns a terminal process (for example a collection, rejection or "
            + "handover sent from a scanner) by its UUID: status, fault description, the device and user that sent "
            + "it and the communication steps in chronological order. Use it to explain why a scan failed.")
    public ProcessToolResponse getProcess(
            @ToolParam(description = "Process identifier (UUID), e.g. the processId of a shipment event")
            final String processId) {
        return executor.execute("get_process",
                () -> responseMapper.map(mcpQueryPort.findProcess(arguments.processId(processId))));
    }

    @Tool(name = "list_devices", description = "Lists the operator's terminals, scanners and mobile devices with "
            + "status, department, assignment mode (PERSONAL or SHARED), app version, last heartbeat and the "
            + "operator currently signed in. Returns at most 50 devices. Requires the MANAGER role or higher.")
    public DeviceListToolResponse listDevices(
            @ToolParam(required = false, description = "Optional device status filter: ACTIVE, BLOCKED, LOST or RETIRED")
            final String status,
            @ToolParam(required = false, description = "Optional department code filter, e.g. KT1")
            final String departmentCode) {
        return executor.execute("list_devices",
                () -> responseMapper.map(mcpQueryPort.listDevices(new DeviceFilter(status, departmentCode))));
    }

    @Tool(name = "get_shipment_route", description = "Returns the planned route of a shipment through the delivery "
            + "network: the departments it passes from origin to destination, in order, and the total distance in "
            + "kilometres. Use it to explain where a shipment goes next.")
    public ShipmentRouteToolResponse getShipmentRoute(
            @ToolParam(description = "Shipment tracking number exactly as printed on the label")
            final String trackingNumber) {
        return executor.execute("get_shipment_route", () -> {
            final TrackingNumber number = arguments.trackingNumber(trackingNumber);
            return responseMapper.map(number, mcpQueryPort.findShipmentRoute(number));
        });
    }

    @Tool(name = "list_departments", description = "Lists the operator's departments with code, type (for example "
            + "BRANCH, WAREHOUSE or SORTING_FACILITY) and status. Returns at most 100 departments.")
    public DepartmentListToolResponse listDepartments(
            @ToolParam(required = false, description = "Optional status filter: ACTIVE, INACTIVE, ARCHIVED or SUSPENDED")
            final String status,
            @ToolParam(required = false, description = "Optional department type filter, e.g. BRANCH or WAREHOUSE")
            final String type) {
        return executor.execute("list_departments",
                () -> responseMapper.map(mcpQueryPort.listDepartments(new DepartmentFilter(status, type))));
    }

    @Tool(name = "get_department", description = "Returns a department by its code: city, street, postal code, "
            + "country and coordinates.")
    public DepartmentDetailsToolResponse getDepartment(
            @ToolParam(description = "Department code, e.g. KT1")
            final String departmentCode) {
        return executor.execute("get_department",
                () -> responseMapper.map(mcpQueryPort.findDepartment(arguments.departmentCode(departmentCode))));
    }

    @Tool(name = "list_deliveries", description = "Lists the newest deliveries (shipment and pallet transports) with "
            + "status, origin and destination department, courier, delivery method and steps. Filter by department "
            + "or courier. Returns 25 deliveries by default, at most 100.")
    public DeliveryListToolResponse listDeliveries(
            @ToolParam(required = false, description = "Optional department code filter, e.g. KT1")
            final String departmentCode,
            @ToolParam(required = false, description = "Optional courier (supplier) code filter")
            final String courierCode,
            @ToolParam(required = false, description = "Optional number of deliveries to return, 1-100")
            final Integer limit) {
        return executor.execute("list_deliveries", () -> responseMapper.map(mcpQueryPort.listDeliveries(
                new DeliveryFilter(arguments.optionalDepartmentCode(departmentCode),
                        arguments.optionalCourierCode(courierCode), limit))));
    }

    @Tool(name = "get_delivery", description = "Returns one delivery with its route, courier, conditions and all "
            + "delivery steps. Pass either the deliveryId or the shipment trackingNumber.")
    public DeliveryToolResponse getDelivery(
            @ToolParam(required = false, description = "Delivery identifier from list_deliveries")
            final String deliveryId,
            @ToolParam(required = false, description = "Shipment tracking number, used when deliveryId is not given")
            final String trackingNumber) {
        return executor.execute("get_delivery", () -> arguments.optional(deliveryId) != null
                ? responseMapper.map(mcpQueryPort.findDelivery(arguments.deliveryId(deliveryId)))
                : responseMapper.map(mcpQueryPort.findShipmentDelivery(arguments.trackingNumber(trackingNumber))));
    }

    @Tool(name = "list_couriers", description = "Lists the couriers (suppliers) of the caller's department with "
            + "code, name, phone number and status. Requires the MANAGER role or higher.")
    public CourierListToolResponse listCouriers() {
        return executor.execute("list_couriers", () -> responseMapper.map(mcpQueryPort.listCouriers()));
    }

    @Tool(name = "get_configuration", description = "Returns one section of the operator configuration as JSON: "
            + "SHIPMENT, DEPARTMENTS, FLEET, COMMUNICATION, REGIONAL, SORTING or PICKUP_POINTS. Use it before "
            + "update_configuration to see the field names and current values.")
    public ConfigurationToolResponse getConfiguration(
            @ToolParam(description = "Configuration section, e.g. FLEET")
            final String section) {
        return executor.execute("get_configuration",
                () -> responseMapper.map(mcpQueryPort.findConfiguration(ConfigurationSection.from(section))));
    }
}
