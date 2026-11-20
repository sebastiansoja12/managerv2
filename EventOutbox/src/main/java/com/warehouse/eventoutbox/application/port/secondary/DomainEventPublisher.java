package com.warehouse.eventoutbox.application.port.secondary;

import com.warehouse.eventoutbox.domain.model.DomainEvent;

public interface DomainEventPublisher {

    void publish(DomainEvent event);
}
