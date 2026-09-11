package com.warehouse.deliveryhandover.application.port.primary;

import java.util.List;

import com.warehouse.deliveryhandover.application.port.primary.command.DeliveryHandoverCommand;
import com.warehouse.deliveryhandover.application.port.primary.result.DeliveryHandoverResult;
import com.warehouse.deliveryhandover.application.port.secondary.ShipmentHandoverServicePort;
import com.warehouse.deliveryhandover.application.port.secondary.SupplierConfigurationServicePort;
import com.warehouse.deliveryhandover.domain.model.DeliveryHandover;
import com.warehouse.deliveryhandover.domain.vo.HandoverResult;
import com.warehouse.deliveryhandover.domain.vo.RecipientConfirmation;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryHandoverPortImpl implements DeliveryHandoverPort {

    private final ShipmentHandoverServicePort shipmentHandoverServicePort;

    private final SupplierConfigurationServicePort supplierConfigurationServicePort;

    public DeliveryHandoverPortImpl(final ShipmentHandoverServicePort shipmentHandoverServicePort,
                                    final SupplierConfigurationServicePort supplierConfigurationServicePort) {
        this.shipmentHandoverServicePort = shipmentHandoverServicePort;
        this.supplierConfigurationServicePort = supplierConfigurationServicePort;
    }

    @Override
    public DeliveryHandoverResult handOver(final DeliveryHandoverCommand command) {
        final DeliveryHandover deliveryHandover = new DeliveryHandover(command.processId(), command.trackingNumbers(),
                new RecipientConfirmation(command.recipientName(), command.signature()));
        final List<HandoverResult> results = deliveryHandover.handOver(
                supplierConfigurationServicePort.isRecipientSignatureRequired(),
                trackingNumber -> shipmentHandoverServicePort.handOver(deliveryHandover.processId(), trackingNumber,
                        deliveryHandover.recipientConfirmation()));
        final long handedOverCount = results.stream()
                .filter(HandoverResult::handedOver)
                .count();
        log.info("Handover process {} finished: {} of {} shipments handed over",
                deliveryHandover.processId().value(), handedOverCount, results.size());
        return new DeliveryHandoverResult(results, deliveryHandover.recipientConfirmation().signed());
    }
}
