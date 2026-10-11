package com.warehouse.mcp.infrastructure.adapter.primary;

import java.math.BigDecimal;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.mcp.application.port.primary.McpCommandPort;
import com.warehouse.mcp.domain.vo.ConfigurationSection;
import com.warehouse.mcp.domain.vo.CourierChange;
import com.warehouse.mcp.domain.vo.CourierDraft;
import com.warehouse.mcp.domain.vo.DeliveryConditions;
import com.warehouse.mcp.domain.vo.DeliveryRouteChange;
import com.warehouse.mcp.domain.vo.DepartmentAddress;
import com.warehouse.mcp.domain.vo.ShipmentDraft;
import com.warehouse.mcp.domain.vo.ShipmentParty;
import com.warehouse.mcp.domain.vo.ShipmentPartyChange;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ChangeToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ConfigurationToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DeliveryToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.DepartmentDetailsToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentCreatedToolResponse;
import com.warehouse.mcp.infrastructure.adapter.primary.response.ShipmentToolResponse;

public class ManagerMcpCommandTools {

    private static final String CHANGES_DATA = " Changes data: confirm the change with the user before calling.";

    private final McpCommandPort mcpCommandPort;

    private final McpToolResponseMapper responseMapper;

    private final McpToolExecutor executor;

    private final McpToolArguments arguments;

    public ManagerMcpCommandTools(final McpCommandPort mcpCommandPort,
                                  final McpToolResponseMapper responseMapper,
                                  final McpToolExecutor executor,
                                  final McpToolArguments arguments) {
        this.mcpCommandPort = mcpCommandPort;
        this.responseMapper = responseMapper;
        this.executor = executor;
        this.arguments = arguments;
    }

    @Tool(name = "assign_delivery_courier", description = "Assigns a courier (and optionally a vehicle) to a "
            + "delivery. Returns the updated delivery." + CHANGES_DATA)
    public DeliveryToolResponse assignDeliveryCourier(
            @ToolParam(description = "Delivery identifier") final String deliveryId,
            @ToolParam(description = "Courier (supplier) code") final String courierCode,
            @ToolParam(required = false, description = "Optional vehicle identifier") final Long vehicleId) {
        return executor.execute("assign_delivery_courier", () -> responseMapper.map(mcpCommandPort.assignCourier(
                arguments.deliveryId(deliveryId), arguments.courierCode(courierCode),
                vehicleId != null ? new VehicleId(vehicleId) : null)));
    }

    @Tool(name = "unassign_delivery_courier", description = "Removes the courier from a delivery. Returns the "
            + "updated delivery." + CHANGES_DATA)
    public DeliveryToolResponse unassignDeliveryCourier(
            @ToolParam(description = "Delivery identifier") final String deliveryId) {
        return executor.execute("unassign_delivery_courier",
                () -> responseMapper.map(mcpCommandPort.unassignCourier(arguments.deliveryId(deliveryId))));
    }

    @Tool(name = "change_delivery_route", description = "Changes the route of a delivery. Only the given fields "
            + "change; the others keep their current values. Pickup methods: DEPARTMENT, COURIER, PICKUP_POINT, "
            + "LOCKER." + CHANGES_DATA)
    public DeliveryToolResponse changeDeliveryRoute(
            @ToolParam(description = "Delivery identifier") final String deliveryId,
            @ToolParam(required = false, description = "New origin department code") final String originDepartmentCode,
            @ToolParam(required = false, description = "New destination department code")
            final String destinationDepartmentCode,
            @ToolParam(required = false, description = "New pickup method") final String pickupMethod,
            @ToolParam(required = false, description = "Pickup point UUID for pickup") final String pickupPointId,
            @ToolParam(required = false, description = "Pickup point UUID for delivery")
            final String deliveryPickupPointId) {
        return executor.execute("change_delivery_route", () -> responseMapper.map(mcpCommandPort.changeDeliveryRoute(
                arguments.deliveryId(deliveryId), new DeliveryRouteChange(
                        arguments.optionalDepartmentCode(originDepartmentCode),
                        arguments.optionalDepartmentCode(destinationDepartmentCode), arguments.upper(pickupMethod),
                        arguments.optional(pickupPointId), arguments.optional(deliveryPickupPointId)))));
    }

    @Tool(name = "change_delivery_conditions", description = "Changes delivery conditions: method (COURIER, "
            + "PICKUP_POINT or LOCKER), signature requirement, instructions for the courier and planned date. "
            + "Only the given fields change." + CHANGES_DATA)
    public DeliveryToolResponse changeDeliveryConditions(
            @ToolParam(description = "Delivery identifier") final String deliveryId,
            @ToolParam(required = false, description = "Delivery method") final String method,
            @ToolParam(required = false, description = "Whether the recipient must sign")
            final Boolean signatureRequired,
            @ToolParam(required = false, description = "Instructions for the courier") final String instructions,
            @ToolParam(required = false, description = "Planned date and time, ISO-8601, e.g. 2026-11-05T10:00")
            final String plannedAt) {
        return executor.execute("change_delivery_conditions", () -> responseMapper.map(
                mcpCommandPort.changeDeliveryConditions(arguments.deliveryId(deliveryId), new DeliveryConditions(
                        arguments.upper(method), signatureRequired, arguments.optional(instructions),
                        arguments.dateTime(plannedAt)))));
    }

    @Tool(name = "comment_delivery_step", description = "Adds or replaces the comment of a delivery step (steps are "
            + "numbered from 1, see get_delivery)." + CHANGES_DATA)
    public DeliveryToolResponse commentDeliveryStep(
            @ToolParam(description = "Delivery identifier") final String deliveryId,
            @ToolParam(description = "Step number") final Integer stepNumber,
            @ToolParam(description = "Comment text") final String comment) {
        return executor.execute("comment_delivery_step", () -> responseMapper.map(mcpCommandPort.commentDeliveryStep(
                arguments.deliveryId(deliveryId), arguments.stepNumber(stepNumber),
                arguments.required(comment, "comment"))));
    }

    @Tool(name = "correct_delivery_step_status", description = "Corrects the delivery status recorded on a delivery "
            + "step, e.g. DELIVERED or a failure status shown by get_delivery." + CHANGES_DATA)
    public DeliveryToolResponse correctDeliveryStepStatus(
            @ToolParam(description = "Delivery identifier") final String deliveryId,
            @ToolParam(description = "Step number") final Integer stepNumber,
            @ToolParam(description = "New delivery status") final String deliveryStatus) {
        return executor.execute("correct_delivery_step_status", () -> responseMapper.map(
                mcpCommandPort.correctDeliveryStepStatus(arguments.deliveryId(deliveryId),
                        arguments.stepNumber(stepNumber),
                        arguments.upper(arguments.required(deliveryStatus, "deliveryStatus")))));
    }

    @Tool(name = "cancel_delivery", description = "Cancels a delivery. This cannot be undone." + CHANGES_DATA)
    public ChangeToolResponse cancelDelivery(
            @ToolParam(description = "Delivery identifier") final String deliveryId) {
        return executor.execute("cancel_delivery", () -> {
            final DeliveryId id = arguments.deliveryId(deliveryId);
            mcpCommandPort.cancelDelivery(id);
            return ChangeToolResponse.done("Delivery " + id.getId() + " cancelled");
        });
    }

    @Tool(name = "create_shipment", description = "Creates a shipment and returns its tracking number. Telephone "
            + "numbers have exactly 9 digits. Dimensions are in cm, weight in kg. Pickup methods: DEPARTMENT, "
            + "COURIER, PICKUP_POINT, LOCKER; delivery methods: COURIER, PICKUP_POINT, LOCKER (the last two need "
            + "deliveryPickupPointId)." + CHANGES_DATA)
    public ShipmentCreatedToolResponse createShipment(
            @ToolParam(description = "Sender first name") final String senderFirstName,
            @ToolParam(description = "Sender last name") final String senderLastName,
            @ToolParam(description = "Sender email") final String senderEmail,
            @ToolParam(description = "Sender telephone number, 9 digits") final String senderTelephoneNumber,
            @ToolParam(description = "Sender city") final String senderCity,
            @ToolParam(description = "Sender postal code") final String senderPostalCode,
            @ToolParam(description = "Sender street and number") final String senderStreet,
            @ToolParam(required = false, description = "Sender country code, default PL")
            final String senderCountryCode,
            @ToolParam(description = "Recipient first name") final String recipientFirstName,
            @ToolParam(description = "Recipient last name") final String recipientLastName,
            @ToolParam(description = "Recipient email") final String recipientEmail,
            @ToolParam(description = "Recipient telephone number, 9 digits") final String recipientTelephoneNumber,
            @ToolParam(description = "Recipient city") final String recipientCity,
            @ToolParam(description = "Recipient postal code") final String recipientPostalCode,
            @ToolParam(description = "Recipient street and number") final String recipientStreet,
            @ToolParam(required = false, description = "Recipient country code, default PL")
            final String recipientCountryCode,
            @ToolParam(description = "Length in cm") final BigDecimal lengthCm,
            @ToolParam(description = "Width in cm") final BigDecimal widthCm,
            @ToolParam(description = "Height in cm") final BigDecimal heightCm,
            @ToolParam(description = "Weight in kg") final BigDecimal weightKg,
            @ToolParam(description = "Shipment price") final BigDecimal price,
            @ToolParam(required = false, description = "Currency, default PLN") final String currency,
            @ToolParam(required = false, description = "Declared value of the content") final BigDecimal declaredValue,
            @ToolParam(required = false, description = "Content description") final String contentDescription,
            @ToolParam(required = false, description = "Customer reference") final String customerReference,
            @ToolParam(required = false, description = "Priority: LOW, MEDIUM, HIGH or EXPRESS") final String priority,
            @ToolParam(required = false, description = "Packaging: ENVELOPE, BAG, BOX, TUBE, CRATE or OTHER")
            final String packagingType,
            @ToolParam(required = false, description = "Service level: ECONOMY, STANDARD or EXPRESS")
            final String serviceLevel,
            @ToolParam(required = false, description = "Pickup method") final String pickupMethod,
            @ToolParam(required = false, description = "Delivery method") final String deliveryMethod,
            @ToolParam(required = false, description = "Pickup point UUID for pickup") final String pickupPointId,
            @ToolParam(required = false, description = "Pickup point UUID for delivery")
            final String deliveryPickupPointId) {
        return executor.execute("create_shipment", () -> new ShipmentCreatedToolResponse(mcpCommandPort.createShipment(
                new ShipmentDraft(
                        new ShipmentParty(arguments.optional(senderFirstName), arguments.optional(senderLastName),
                                arguments.optional(senderEmail), arguments.optional(senderTelephoneNumber),
                                arguments.optional(senderCity), arguments.optional(senderPostalCode),
                                arguments.optional(senderStreet), arguments.countryCode(senderCountryCode)),
                        new ShipmentParty(arguments.optional(recipientFirstName), arguments.optional(recipientLastName),
                                arguments.optional(recipientEmail), arguments.optional(recipientTelephoneNumber),
                                arguments.optional(recipientCity), arguments.optional(recipientPostalCode),
                                arguments.optional(recipientStreet), arguments.countryCode(recipientCountryCode)),
                        lengthCm, widthCm, heightCm, weightKg, arguments.optional(contentDescription), price,
                        declaredValue, arguments.currency(currency), arguments.optional(customerReference),
                        arguments.upper(priority), arguments.upper(packagingType), arguments.upper(serviceLevel),
                        arguments.upper(pickupMethod), arguments.upper(deliveryMethod),
                        arguments.optional(pickupPointId), arguments.optional(deliveryPickupPointId)))
                .value()));
    }

    @Tool(name = "change_shipment_party", description = "Changes the sender or recipient data of a shipment. Only "
            + "the given fields change. Returns the shipment with masked personal data." + CHANGES_DATA)
    public ShipmentToolResponse changeShipmentParty(
            @ToolParam(description = "Shipment tracking number") final String trackingNumber,
            @ToolParam(description = "SENDER or RECIPIENT") final String party,
            @ToolParam(required = false, description = "First name") final String firstName,
            @ToolParam(required = false, description = "Last name") final String lastName,
            @ToolParam(required = false, description = "Email") final String email,
            @ToolParam(required = false, description = "Telephone number, 9 digits") final String telephoneNumber,
            @ToolParam(required = false, description = "City") final String city,
            @ToolParam(required = false, description = "Postal code") final String postalCode,
            @ToolParam(required = false, description = "Street and number") final String street,
            @ToolParam(required = false, description = "Country code, e.g. PL") final String countryCode) {
        return executor.execute("change_shipment_party", () -> responseMapper.map(mcpCommandPort.changeShipmentParty(
                arguments.trackingNumber(trackingNumber), arguments.partyRole(party),
                new ShipmentPartyChange(arguments.optional(firstName), arguments.optional(lastName),
                        arguments.optional(email), arguments.optional(telephoneNumber), arguments.optional(city),
                        arguments.optional(postalCode), arguments.optional(street), arguments.upper(countryCode)))));
    }

    @Tool(name = "change_department_address", description = "Changes the address of a department. Returns the "
            + "updated department." + CHANGES_DATA)
    public DepartmentDetailsToolResponse changeDepartmentAddress(
            @ToolParam(description = "Department code, e.g. KT1") final String departmentCode,
            @ToolParam(description = "City") final String city,
            @ToolParam(description = "Street and number") final String street,
            @ToolParam(description = "Postal code") final String postalCode,
            @ToolParam(description = "Country code, e.g. PL") final String countryCode) {
        return executor.execute("change_department_address", () -> responseMapper.map(
                mcpCommandPort.changeDepartmentAddress(arguments.departmentCode(departmentCode), new DepartmentAddress(
                        arguments.optional(city), arguments.optional(street), arguments.optional(postalCode),
                        arguments.upper(countryCode)))));
    }

    @Tool(name = "change_department_type", description = "Changes the department type, e.g. BRANCH, WAREHOUSE, "
            + "DISTRIBUTION or SORTING_FACILITY." + CHANGES_DATA)
    public DepartmentDetailsToolResponse changeDepartmentType(
            @ToolParam(description = "Department code") final String departmentCode,
            @ToolParam(description = "New department type") final String type) {
        return executor.execute("change_department_type", () -> responseMapper.map(
                mcpCommandPort.changeDepartmentType(arguments.departmentCode(departmentCode),
                        arguments.upper(arguments.required(type, "type")))));
    }

    @Tool(name = "change_department_email", description = "Changes the contact email of a department." + CHANGES_DATA)
    public DepartmentDetailsToolResponse changeDepartmentEmail(
            @ToolParam(description = "Department code") final String departmentCode,
            @ToolParam(description = "New email") final String email) {
        return executor.execute("change_department_email", () -> responseMapper.map(
                mcpCommandPort.changeDepartmentEmail(arguments.departmentCode(departmentCode),
                        arguments.optional(email))));
    }

    @Tool(name = "change_department_status", description = "Changes the department status: ACTIVE, INACTIVE, "
            + "SUSPENDED or ARCHIVED. Requires the ADMIN role or higher." + CHANGES_DATA)
    public DepartmentDetailsToolResponse changeDepartmentStatus(
            @ToolParam(description = "Department code") final String departmentCode,
            @ToolParam(description = "New status") final String status) {
        return executor.execute("change_department_status", () -> responseMapper.map(
                mcpCommandPort.changeDepartmentStatus(arguments.departmentCode(departmentCode),
                        arguments.upper(arguments.required(status, "status")))));
    }

    @Tool(name = "set_department_active", description = "Activates or deactivates a department." + CHANGES_DATA)
    public DepartmentDetailsToolResponse setDepartmentActive(
            @ToolParam(description = "Department code") final String departmentCode,
            @ToolParam(description = "true to activate, false to deactivate") final Boolean active) {
        return executor.execute("set_department_active", () -> responseMapper.map(
                mcpCommandPort.changeDepartmentActive(arguments.departmentCode(departmentCode),
                        arguments.required(active, "active"))));
    }

    @Tool(name = "create_courier", description = "Adds a courier (supplier) and creates the courier's user account, "
            + "which needs an email. Without departmentCode the caller's department is used. Returns the courier "
            + "code." + CHANGES_DATA)
    public ChangeToolResponse createCourier(
            @ToolParam(description = "First name") final String firstName,
            @ToolParam(description = "Last name") final String lastName,
            @ToolParam(description = "Telephone number") final String telephoneNumber,
            @ToolParam(description = "Email for the courier's user account") final String email,
            @ToolParam(required = false, description = "Department code") final String departmentCode,
            @ToolParam(required = false, description = "Courier code; generated when omitted")
            final String courierCode) {
        return executor.execute("create_courier", () -> {
            final SupplierCode created = mcpCommandPort.createCourier(new CourierDraft(
                    arguments.optionalCourierCode(courierCode), arguments.required(firstName, "firstName"),
                    arguments.required(lastName, "lastName"), arguments.required(telephoneNumber, "telephoneNumber"),
                    arguments.optional(email), arguments.optionalDepartmentCode(departmentCode)));
            return ChangeToolResponse.done("Courier " + created.value() + " created");
        });
    }

    @Tool(name = "update_courier", description = "Changes a courier's first name, last name or telephone number. "
            + "Only the given fields change." + CHANGES_DATA)
    public ChangeToolResponse updateCourier(
            @ToolParam(description = "Courier (supplier) code") final String courierCode,
            @ToolParam(required = false, description = "First name") final String firstName,
            @ToolParam(required = false, description = "Last name") final String lastName,
            @ToolParam(required = false, description = "Telephone number") final String telephoneNumber) {
        return executor.execute("update_courier", () -> {
            final SupplierCode code = arguments.courierCode(courierCode);
            mcpCommandPort.updateCourier(new CourierChange(code, arguments.optional(firstName),
                    arguments.optional(lastName), arguments.optional(telephoneNumber)));
            return ChangeToolResponse.done("Courier " + code.value() + " updated");
        });
    }

    @Tool(name = "set_courier_active", description = "Activates or deactivates a courier." + CHANGES_DATA)
    public ChangeToolResponse setCourierActive(
            @ToolParam(description = "Courier (supplier) code") final String courierCode,
            @ToolParam(description = "true to activate, false to deactivate") final Boolean active) {
        return executor.execute("set_courier_active", () -> {
            final SupplierCode code = arguments.courierCode(courierCode);
            final boolean activate = arguments.required(active, "active");
            mcpCommandPort.changeCourierActive(code, activate);
            return ChangeToolResponse.done("Courier " + code.value() + (activate ? " activated" : " deactivated"));
        });
    }

    @Tool(name = "update_configuration", description = "Changes one section of the operator configuration. Pass "
            + "only the fields to change as a JSON object, nested like in get_configuration, e.g. "
            + "{\"maxVehicles\": 20}. Unknown fields are rejected. Returns the whole updated section." + CHANGES_DATA)
    public ConfigurationToolResponse updateConfiguration(
            @ToolParam(description = "Configuration section: SHIPMENT, DEPARTMENTS, FLEET, COMMUNICATION, REGIONAL, "
                    + "SORTING or PICKUP_POINTS") final String section,
            @ToolParam(description = "JSON object with the fields to change") final String changes) {
        return executor.execute("update_configuration", () -> responseMapper.map(mcpCommandPort.updateConfiguration(
                ConfigurationSection.from(section), arguments.jsonObject(changes, "changes"))));
    }
}
