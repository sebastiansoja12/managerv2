package com.warehouse.pickuppoint.application.listener;

import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.api.event.PickupPointReadModelChangedIntegrationEvent;
import com.warehouse.pickuppoint.api.event.snapshot.PickupPointReadModelData;
import com.warehouse.pickuppoint.domain.event.PickupPointChanged;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class PickupPointReadModelIntegrationEventTranslator {

    @IntegrationEventTranslation
    public PickupPointReadModelChangedIntegrationEvent translate(final PickupPointChanged event) {
        return new PickupPointReadModelChangedIntegrationEvent(
                new PickupPointReadModelData(
                        new PickupPointIdDto(event.getSnapshot().pickupPointId().value())),
                event.getTimestamp());
    }
}
