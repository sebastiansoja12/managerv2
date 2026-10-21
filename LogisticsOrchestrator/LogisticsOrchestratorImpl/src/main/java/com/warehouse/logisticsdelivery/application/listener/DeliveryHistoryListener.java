package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.logisticsdelivery.application.port.primary.DeliveryHistoryPort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryHistoryListener {

    private final DeliveryHistoryPort deliveryHistoryPort;

    public DeliveryHistoryListener(final DeliveryHistoryPort deliveryHistoryPort) {
        this.deliveryHistoryPort = deliveryHistoryPort;
    }

    @EventListener
    public void handle(final DeliveryChanged event) {
        this.deliveryHistoryPort.record(event);
    }
}
