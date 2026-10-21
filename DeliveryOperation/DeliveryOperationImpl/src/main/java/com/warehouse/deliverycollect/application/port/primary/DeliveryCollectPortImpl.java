package com.warehouse.deliverycollect.application.port.primary;

import java.util.List;

import com.warehouse.deliverycollect.application.port.primary.command.DeliveryCollectCommand;
import com.warehouse.deliverycollect.application.port.primary.result.DeliveryCollectResult;
import com.warehouse.deliverycollect.application.port.secondary.ShipmentCollectServicePort;
import com.warehouse.deliverycollect.domain.model.DeliveryCollect;
import com.warehouse.deliverycollect.domain.vo.CollectResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DeliveryCollectPortImpl implements DeliveryCollectPort {

    private final ShipmentCollectServicePort shipmentCollectServicePort;

    public DeliveryCollectPortImpl(final ShipmentCollectServicePort shipmentCollectServicePort) {
        this.shipmentCollectServicePort = shipmentCollectServicePort;
    }

    @Override
    public DeliveryCollectResult collect(final DeliveryCollectCommand command) {
        final DeliveryCollect deliveryCollect = new DeliveryCollect(command.processId(), command.trackingNumbers());
        final List<CollectResult> results = deliveryCollect.collect(trackingNumber ->
                shipmentCollectServicePort.collect(deliveryCollect.processId(), trackingNumber));
        final long collectedCount = results.stream()
                .filter(CollectResult::collected)
                .count();
        log.info("Collect process {} finished: {} of {} shipments collected",
                deliveryCollect.processId().value(), collectedCount, results.size());
        return new DeliveryCollectResult(results);
    }
}
