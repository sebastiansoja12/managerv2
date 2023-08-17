package com.warehouse.redirect.domain.service;

import java.time.Instant;

import com.warehouse.redirect.infrastructure.adapter.secondary.RedirectTokenReadRepository;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@EqualsAndHashCode(callSuper = true)
@Slf4j
@Data
public record ExpiredRedirectTokenJobDeleter(RedirectTokenReadRepository repository) {

    @Scheduled(cron = "${purge.cron.expression}")
    public void deleteAllExpiredSince() {
        log.warn("Cleanup database from expired redirect tokens");
        repository.deleteAllExpiredSince(Instant.now());
        log.info("Expired redirect tokens have been successfully deleted");
    }
}
