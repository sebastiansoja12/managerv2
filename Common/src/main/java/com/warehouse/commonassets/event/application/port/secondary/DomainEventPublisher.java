package com.warehouse.commonassets.event.application.port.secondary;

import com.warehouse.commonassets.event.domain.model.DomainEvent;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
