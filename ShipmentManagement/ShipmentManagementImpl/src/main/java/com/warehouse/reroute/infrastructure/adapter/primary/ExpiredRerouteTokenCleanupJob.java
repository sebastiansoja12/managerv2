package com.warehouse.reroute.infrastructure.adapter.primary;

import org.springframework.scheduling.annotation.Scheduled;

import com.warehouse.reroute.application.port.primary.RerouteTokenPort;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExpiredRerouteTokenCleanupJob {

    private final RerouteTokenPort rerouteTokenPort;

    public ExpiredRerouteTokenCleanupJob(final RerouteTokenPort rerouteTokenPort) {
        this.rerouteTokenPort = rerouteTokenPort;
    }

    @Scheduled(cron = "${purge.cron.expression}")
    public void removeExpiredTokens() {
        rerouteTokenPort.removeExpiredTokens();
        log.info("Expired reroute tokens have been removed");
    }
}
