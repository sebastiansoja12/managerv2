package com.warehouse.deliverynetwork.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.application.port.primary.result.ShipmentDeliveryRouteResult;
import com.warehouse.deliverynetwork.application.port.secondary.DepartmentDirectoryServicePort;
import com.warehouse.deliverynetwork.application.port.secondary.ShipmentRouteDocumentRendererServicePort;
import com.warehouse.deliverynetwork.domain.service.ShipmentHandlingPlanFactory;
import com.warehouse.deliverynetwork.domain.vo.DepartmentAddress;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentData;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentFile;

import java.time.Instant;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ShipmentRouteDocumentService {

    private final DepartmentDirectoryServicePort departmentDirectoryServicePort;

    private final ShipmentRouteDocumentRendererServicePort shipmentRouteDocumentRendererServicePort;

    private final ShipmentHandlingPlanFactory shipmentHandlingPlanFactory;

    public ShipmentRouteDocumentService(
            final DepartmentDirectoryServicePort departmentDirectoryServicePort,
            final ShipmentRouteDocumentRendererServicePort shipmentRouteDocumentRendererServicePort,
            final ShipmentHandlingPlanFactory shipmentHandlingPlanFactory) {
        this.departmentDirectoryServicePort = departmentDirectoryServicePort;
        this.shipmentRouteDocumentRendererServicePort = shipmentRouteDocumentRendererServicePort;
        this.shipmentHandlingPlanFactory = shipmentHandlingPlanFactory;
    }

    public ShipmentRouteDocumentFile routeDocument(final ShipmentDeliveryRouteResult shipmentDeliveryRoute,
                                                   final Locale locale) {
        return this.shipmentRouteDocumentRendererServicePort.renderRouteDocument(data(shipmentDeliveryRoute, locale));
    }

    public ShipmentRouteDocumentFile handlingPlan(final ShipmentDeliveryRouteResult shipmentDeliveryRoute,
                                                  final Locale locale) {
        return this.shipmentRouteDocumentRendererServicePort.renderHandlingPlan(data(shipmentDeliveryRoute, locale));
    }

    private ShipmentRouteDocumentData data(final ShipmentDeliveryRouteResult shipmentDeliveryRoute,
                                           final Locale locale) {
        final Map<DepartmentId, DepartmentAddress> addresses = new HashMap<>();
        for (final DepartmentNode department : shipmentDeliveryRoute.departments()) {
            this.departmentDirectoryServicePort.findDepartmentAddress(department.departmentId())
                    .ifPresent(address -> addresses.put(department.departmentId(), address));
        }
        return new ShipmentRouteDocumentData(
                shipmentDeliveryRoute.shipmentId(),
                shipmentDeliveryRoute.trackingNumber(),
                shipmentDeliveryRoute.departments(),
                addresses,
                this.shipmentHandlingPlanFactory.create(shipmentDeliveryRoute.departments()),
                shipmentDeliveryRoute.distanceInKilometers(),
                Instant.now(),
                locale);
    }
}
