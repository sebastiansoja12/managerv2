package com.warehouse.mcp.infrastructure.adapter.primary;

import java.util.UUID;
import java.util.function.Supplier;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.application.port.primary.McpQueryPort;
import com.warehouse.mcp.domain.exception.McpToolAccessException;
import com.warehouse.mcp.domain.vo.DeviceFilter;
import com.warehouse.mcp.infrastructure.adapter.primary.response.CurrentUserToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeviceListToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ProcessToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentToolResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ManagerMcpTools {

    private static final String REQUEST_FAILED =
            "The request could not be completed. The resource may not exist or you may not have access to it.";

    private final McpQueryPort mcpQueryPort;

    private final McpToolResponseMapper responseMapper;

    public ManagerMcpTools(final McpQueryPort mcpQueryPort, final McpToolResponseMapper responseMapper) {
        this.mcpQueryPort = mcpQueryPort;
        this.responseMapper = responseMapper;
    }

    @Tool(name = "whoami", description = "Returns the Manager account used by this connection: username, role, "
            + "department code and operator. Call it first to learn which department and permissions apply.")
    public CurrentUserToolResponse whoami() {
        return execute("whoami", () -> responseMapper.map(mcpQueryPort.currentUser()));
    }

    @Tool(name = "find_shipment", description = "Finds a shipment by its tracking number and returns its status, "
            + "dates, masked sender and recipient (initials, city, postal code) and the latest tracking events, "
            + "newest first. Only shipments of the caller's operator are visible.")
    public ShipmentToolResponse findShipment(
            @ToolParam(description = "Shipment tracking number exactly as printed on the label")
            final String trackingNumber) {
        return execute("find_shipment", () -> {
            requireText(trackingNumber, "trackingNumber");
            return responseMapper.map(mcpQueryPort.findShipment(new TrackingNumber(trackingNumber.trim())));
        });
    }

    @Tool(name = "get_process", description = "Returns a terminal process (for example a collection, rejection or "
            + "handover sent from a scanner) by its UUID: status, fault description, the device and user that sent "
            + "it and the communication steps in chronological order. Use it to explain why a scan failed.")
    public ProcessToolResponse getProcess(
            @ToolParam(description = "Process identifier (UUID), e.g. the processId of a shipment event")
            final String processId) {
        return execute("get_process", () -> {
            requireText(processId, "processId");
            return responseMapper.map(mcpQueryPort.findProcess(new ProcessId(parseUuid(processId))));
        });
    }

    @Tool(name = "list_devices", description = "Lists the operator's terminals, scanners and mobile devices with "
            + "status, department, assignment mode (PERSONAL or SHARED), app version, last heartbeat and the "
            + "operator currently signed in. Returns at most 50 devices. Requires the MANAGER role or higher.")
    public DeviceListToolResponse listDevices(
            @ToolParam(required = false, description = "Optional device status filter: ACTIVE, BLOCKED, LOST or RETIRED")
            final String status,
            @ToolParam(required = false, description = "Optional department code filter, e.g. KT1")
            final String departmentCode) {
        return execute("list_devices",
                () -> responseMapper.map(mcpQueryPort.listDevices(new DeviceFilter(status, departmentCode))));
    }

    private <T> T execute(final String toolName, final Supplier<T> operation) {
        log.info("MCP tool {} invoked", toolName);
        try {
            return operation.get();
        } catch (final McpToolAccessException | McpToolFailure exception) {
            log.warn("MCP tool {} rejected: {}", toolName, exception.getMessage());
            throw new McpToolFailure(exception.getMessage());
        } catch (final RuntimeException exception) {
            log.warn("MCP tool {} failed with {}", toolName, exception.getClass().getSimpleName());
            throw new McpToolFailure(REQUEST_FAILED);
        }
    }

    private void requireText(final String value, final String parameterName) {
        if (value == null || value.isBlank()) {
            throw new McpToolFailure("Parameter " + parameterName + " is required");
        }
    }

    private UUID parseUuid(final String value) {
        try {
            return UUID.fromString(value.trim());
        } catch (final IllegalArgumentException exception) {
            throw new McpToolFailure("Parameter processId must be a UUID");
        }
    }
}
