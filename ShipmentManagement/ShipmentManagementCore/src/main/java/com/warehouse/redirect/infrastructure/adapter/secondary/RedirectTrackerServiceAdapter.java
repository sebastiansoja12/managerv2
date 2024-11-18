package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.redirect.domain.port.secondary.RedirectTrackerServicePort;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RedirectTrackerServiceAdapter implements RedirectTrackerServicePort {

	@Override
	public void sendRedirectRequest() {
		log.info("Sending redirect request");
	}
}
