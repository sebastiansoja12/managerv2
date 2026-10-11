package com.warehouse.mcp.infrastructure.adapter.primary;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.mcp.domain.vo.PartyRole;

public class McpToolArguments {

    private static final String DEFAULT_COUNTRY_CODE = "PL";

    private static final String DEFAULT_CURRENCY = "PLN";

    private static final TypeReference<Map<String, Object>> MAP_TYPE = new TypeReference<>() {
    };

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String required(final String value, final String parameterName) {
        if (value == null || value.isBlank()) {
            throw new McpToolFailure("Parameter " + parameterName + " is required");
        }
        return value.trim();
    }

    public <T> T required(final T value, final String parameterName) {
        if (value == null) {
            throw new McpToolFailure("Parameter " + parameterName + " is required");
        }
        return value;
    }

    public String optional(final String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    public String upper(final String value) {
        final String text = optional(value);
        return text == null ? null : text.toUpperCase(Locale.ROOT);
    }

    public TrackingNumber trackingNumber(final String value) {
        return new TrackingNumber(required(value, "trackingNumber"));
    }

    public ProcessId processId(final String value) {
        try {
            return new ProcessId(UUID.fromString(required(value, "processId")));
        } catch (final IllegalArgumentException exception) {
            throw new McpToolFailure("Parameter processId must be a UUID");
        }
    }

    public DeliveryId deliveryId(final String value) {
        return new DeliveryId(required(value, "deliveryId"));
    }

    public DepartmentCode departmentCode(final String value) {
        return new DepartmentCode(required(value, "departmentCode").toUpperCase(Locale.ROOT));
    }

    public DepartmentCode optionalDepartmentCode(final String value) {
        final String code = upper(value);
        return code == null ? null : new DepartmentCode(code);
    }

    public SupplierCode courierCode(final String value) {
        return new SupplierCode(required(value, "courierCode"));
    }

    public SupplierCode optionalCourierCode(final String value) {
        final String code = optional(value);
        return code == null ? null : new SupplierCode(code);
    }

    public int stepNumber(final Integer value) {
        if (value == null || value < 1) {
            throw new McpToolFailure("Parameter stepNumber must be 1 or greater");
        }
        return value;
    }

    public PartyRole partyRole(final String value) {
        try {
            return PartyRole.valueOf(required(value, "party").toUpperCase(Locale.ROOT));
        } catch (final IllegalArgumentException exception) {
            throw new McpToolFailure("Parameter party must be SENDER or RECIPIENT");
        }
    }

    public LocalDateTime dateTime(final String value) {
        final String text = optional(value);
        if (text == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(text);
        } catch (final DateTimeParseException exception) {
            throw new McpToolFailure("Parameter plannedAt must be an ISO-8601 date and time, e.g. 2026-11-05T10:00");
        }
    }

    public String countryCode(final String value) {
        final String code = upper(value);
        return code != null ? code : DEFAULT_COUNTRY_CODE;
    }

    public String currency(final String value) {
        final String code = upper(value);
        return code != null ? code : DEFAULT_CURRENCY;
    }

    public Map<String, Object> jsonObject(final String value, final String parameterName) {
        try {
            return this.objectMapper.readValue(required(value, parameterName), MAP_TYPE);
        } catch (final JsonProcessingException exception) {
            throw new McpToolFailure("Parameter " + parameterName + " must be a JSON object");
        }
    }
}
