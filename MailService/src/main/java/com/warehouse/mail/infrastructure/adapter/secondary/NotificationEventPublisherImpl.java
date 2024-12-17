package com.warehouse.mail.infrastructure.adapter.secondary;

import org.springframework.context.ApplicationEventPublisher;

import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEvent;
import com.warehouse.mail.infrastructure.adapter.primary.event.NotificationEventPublisher;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class NotificationEventPublisherImpl implements NotificationEventPublisher {

	private final ApplicationEventPublisher eventPublisher;

	public NotificationEventPublisherImpl(final ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}

	@Override
	public void send(final NotificationEvent event) {
		logEvent(event);
		eventPublisher.publishEvent(event);
	}

	private void logEvent(final NotificationEvent event) {
		log.info("Publishing event {}", event.getClass().getSimpleName());
	}
}
