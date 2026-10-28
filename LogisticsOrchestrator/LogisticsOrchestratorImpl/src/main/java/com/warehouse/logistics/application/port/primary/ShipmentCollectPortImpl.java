package com.warehouse.logistics.application.port.primary;

import com.warehouse.logistics.application.port.primary.command.ShipmentCollectCommand;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectDetail;
import com.warehouse.logistics.application.port.primary.result.ShipmentCollectResult;
import com.warehouse.logistics.application.port.secondary.DeliveryCollectServicePort;
import com.warehouse.logistics.application.port.secondary.DeliveryServicePort;
import com.warehouse.logistics.application.port.secondary.SupplierServicePort;
import com.warehouse.logistics.domain.vo.SupplierAssignment;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ShipmentCollectPortImpl implements ShipmentCollectPort {

    private final DeliveryCollectServicePort deliveryCollectServicePort;

    private final DeliveryServicePort deliveryServicePort;

    private final SupplierServicePort supplierServicePort;

    public ShipmentCollectPortImpl(final DeliveryCollectServicePort deliveryCollectServicePort,
                                   final DeliveryServicePort deliveryServicePort,
                                   final SupplierServicePort supplierServicePort) {
        this.deliveryCollectServicePort = deliveryCollectServicePort;
        this.deliveryServicePort = deliveryServicePort;
        this.supplierServicePort = supplierServicePort;
    }

    @Override
    public ShipmentCollectResult collect(final ShipmentCollectCommand command) {
        final ShipmentCollectResult result = deliveryCollectServicePort.collect(command);
        supplierServicePort.findCurrentSupplier().ifPresent(supplier -> result.details()
                .stream()
                .filter(ShipmentCollectDetail::collected)
                .forEach(detail -> assignSupplier(detail, supplier)));
        return result;
    }

    private void assignSupplier(final ShipmentCollectDetail detail, final SupplierAssignment supplier) {
        try {
            deliveryServicePort.assignShipmentSupplier(detail.shipmentId(), supplier.supplierId(),
                    supplier.vehicleId());
        } catch (final RuntimeException exception) {
            log.warn("Supplier {} could not be assigned to delivery of shipment {}: {}",
                    supplier.supplierId().value(), detail.shipmentId().getValue(), exception.getMessage());
        }
    }
}
