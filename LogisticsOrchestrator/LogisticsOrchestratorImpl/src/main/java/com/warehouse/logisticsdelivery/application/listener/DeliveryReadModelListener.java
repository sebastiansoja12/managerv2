package com.warehouse.logisticsdelivery.application.listener;

import com.warehouse.logisticsdelivery.application.port.primary.DeliveryReadModelPort;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class DeliveryReadModelListener {

    private final DeliveryReadModelPort deliveryReadModelPort;

    public DeliveryReadModelListener(final DeliveryReadModelPort deliveryReadModelPort) {
        this.deliveryReadModelPort = deliveryReadModelPort;
    }

    @EventListener
    public void handle(final DeliveryChanged event) {
        this.deliveryReadModelPort.sync(event.getSnapshot());
    }
}
