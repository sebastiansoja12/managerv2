package com.warehouse.mcp.application.port.primary;

import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.mcp.application.port.primary.result.DeliveryOverview;
import com.warehouse.mcp.application.port.secondary.CourierServicePort;
import com.warehouse.mcp.application.port.secondary.DepartmentServicePort;
import com.warehouse.mcp.domain.exception.McpInvalidRequestException;
import com.warehouse.mcp.domain.vo.Courier;
import com.warehouse.mcp.domain.vo.DeliveryDetails;
import com.warehouse.mcp.domain.vo.DepartmentEntry;

class McpReferences {

    private final DepartmentServicePort departmentServicePort;

    private final CourierServicePort courierServicePort;

    McpReferences(final DepartmentServicePort departmentServicePort, final CourierServicePort courierServicePort) {
        this.departmentServicePort = departmentServicePort;
        this.courierServicePort = courierServicePort;
    }

    DepartmentId departmentId(final DepartmentCode departmentCode) {
        if (departmentCode == null) {
            return null;
        }
        return this.departmentServicePort.findAllDepartments().stream()
                .filter(department -> departmentCode.equals(department.departmentCode()))
                .map(DepartmentEntry::departmentId)
                .findFirst()
                .orElseThrow(() -> new McpInvalidRequestException("Unknown department " + departmentCode.value()));
    }

    SupplierId courierId(final SupplierCode courierCode) {
        if (courierCode == null) {
            return null;
        }
        return this.courierServicePort.findCourierId(courierCode)
                .orElseThrow(() -> new McpInvalidRequestException("Unknown courier " + courierCode.value()));
    }

    Function<DeliveryDetails, DeliveryOverview> overviews() {
        final Map<DepartmentId, DepartmentCode> departments = this.departmentServicePort.findAllDepartments().stream()
                .filter(department -> department.departmentId() != null)
                .collect(Collectors.toMap(DepartmentEntry::departmentId, DepartmentEntry::departmentCode,
                        (first, second) -> first));
        final Map<SupplierId, SupplierCode> couriers = this.courierServicePort.findCouriers().stream()
                .filter(courier -> courier.supplierId() != null && courier.courierCode() != null)
                .collect(Collectors.toMap(Courier::supplierId, Courier::courierCode, (first, second) -> first));
        return delivery -> new DeliveryOverview(delivery,
                lookup(departments, delivery.originDepartmentId()),
                lookup(departments, delivery.destinationDepartmentId()),
                lookup(couriers, delivery.supplierId()));
    }

    private <K, V> V lookup(final Map<K, V> values, final K key) {
        return key == null ? null : values.get(Objects.requireNonNull(key));
    }
}
