package com.warehouse.eventoutbox.integration.registry.scanned;

import com.warehouse.eventoutbox.integration.annotation.IntegrationEventType;
import com.warehouse.eventoutbox.integration.model.IntegrationEvent;
import com.warehouse.eventoutbox.integration.model.IntegrationEventKey;

public final class ScannedTestEvents {

    private ScannedTestEvents() {
    }

    @IntegrationEventType(value = "pallet.loaded", version = 1, topic = "pallet.loaded")
    public record PalletLoaded(String palletId) implements IntegrationEvent, IntegrationEventKey {

        @Override
        public String eventKey() {
            return this.palletId;
        }
    }

    @IntegrationEventType(value = "pallet.unloaded", version = 1, topic = "pallet.unloaded")
    public record PalletUnloaded(String palletId) implements IntegrationEvent {
    }

    public record NotDeclaredEvent() implements IntegrationEvent {
    }
}
