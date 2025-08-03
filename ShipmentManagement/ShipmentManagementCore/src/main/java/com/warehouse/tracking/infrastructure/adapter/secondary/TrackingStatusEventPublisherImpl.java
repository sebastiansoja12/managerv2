package com.warehouse.tracking.infrastructure.adapter.secondary;

import com.warehouse.tracking.infrastructure.adapter.primary.api.TrackingStatusEventPublisher;
import com.warehouse.tracking.infrastructure.adapter.primary.api.ShipmentStatusChanged;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;

@Slf4j
public class TrackingStatusEventPublisherImpl implements TrackingStatusEventPublisher {

	private final ApplicationEventPublisher eventPublisher;

	public TrackingStatusEventPublisherImpl(final ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void send(final ShipmentStatusChanged event) {
		logEvent(event);
		eventPublisher.publishEvent(event);
	}

	private void logEvent(final ShipmentStatusChanged event) {
		log.info("Publishing event {}", event.getClass().getSimpleName());
	}
}
