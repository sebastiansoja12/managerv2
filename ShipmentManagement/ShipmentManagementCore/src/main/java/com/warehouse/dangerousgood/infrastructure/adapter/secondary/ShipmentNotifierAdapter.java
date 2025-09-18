package com.warehouse.dangerousgood.infrastructure.adapter.secondary;

import com.warehouse.shipment.infrastructure.adapter.primary.event.DangerousGoodCreated;
import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.dangerousgood.domain.model.DangerousGood;
import com.warehouse.dangerousgood.domain.port.secondary.ShipmentNotifierPort;

import lombok.extern.slf4j.Slf4j;

import java.time.Instant;

@Slf4j
public class ShipmentNotifierAdapter implements ShipmentNotifierPort {

    private final ApplicationEventPublisher eventPublisher;

    public ShipmentNotifierAdapter(final ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void notifyDangerousGoodCreated(final DangerousGood dangerousGood) {
        this.eventPublisher.publishEvent(new DangerousGoodCreated(dangerousGood.toSnapshot(), Instant.now()));
    }
}
