package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.Instant;

@AllArgsConstructor
@Slf4j
public class ExpiredRerouteTokenJobDeleterImpl implements ExpiredRerouteTokenJobDeleter {

    private final RerouteTokenReadRepository repository;

    @Override
    @Scheduled(cron = "${purge.cron.expression}")
    public void deleteAllExpiredSince() {
        log.warn("Cleanup database from expired reroute tokens");
        repository.deleteAllExpiredSince(Instant.now());
        log.info("Expired reroute tokens have been successfully deleted");
    }
}
