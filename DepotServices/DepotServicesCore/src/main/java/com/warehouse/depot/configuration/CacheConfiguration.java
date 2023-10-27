package com.warehouse.depot.configuration;


import java.util.Objects;

import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import lombok.extern.slf4j.Slf4j;

@Configuration
@EnableCaching
@Slf4j
public class CacheConfiguration {


    private final String depotName = "depots";

    private final String depotCode = "depotCode";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager("depotsCache", "depotCodeCache");
    }

    @Scheduled(fixedRate = 3600000)
    @CacheEvict(value = "depotsCache", allEntries = true)
    public void clearDepotsCache() {
        log.info("Cache for {} is being deleted", depotName);
        Objects.requireNonNull(cacheManager().getCache("depotsCache")).clear();
    }

    @Scheduled(fixedRate = 1800000)
    @CacheEvict(value = "depotCodeCache", allEntries = true)
    public void clearDepotCodeCache() {
        log.info("Cache for {} is being deleted", depotCode);
        Objects.requireNonNull(cacheManager().getCache("depotsCache")).clear();
    }

}