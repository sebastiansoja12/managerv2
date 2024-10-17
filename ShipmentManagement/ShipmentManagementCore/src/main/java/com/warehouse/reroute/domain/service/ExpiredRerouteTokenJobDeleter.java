package com.warehouse.reroute.domain.service;

import java.time.Instant;

import org.springframework.scheduling.annotation.Scheduled;

import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class ExpiredRerouteTokenJobDeleter {

    private final RerouteTokenReadRepository repository;

    @Scheduled(cron = "${purge.cron.expression}")
    public void deleteAllExpiredSince() {
        log.warn("Cleanup database from expired reroute tokens");
        repository.deleteAllExpiredSince(Instant.now());
        log.info("Expired reroute tokens have been successfully deleted");
    }
}
