package com.warehouse.cache;


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


    private static final String DEPOTS_CACHE = "depotsCache";

    private static final String DEPOT_CODE = "depotCodeCache";

    private static final String SUPPLIERS_CACHE = "suppliersCache";

    private static final String SUPPLIER_CODE = "supplierCodeCache";

    private static final String SUPPLIERS_CODE = "suppliersCodeCache";

    private static final String SUPPLIERS_DEPOT_CODE = "suppliersDepotCodeCache";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(DEPOTS_CACHE, DEPOT_CODE, SUPPLIERS_CACHE, SUPPLIER_CODE,
                SUPPLIERS_CODE, SUPPLIERS_DEPOT_CODE);
    }

    @Scheduled(fixedRate = 3600000)
    @CacheEvict(value = DEPOTS_CACHE, allEntries = true)
    public void clearDepotsCache() {
        log.info("Cache for {} is being deleted", DEPOTS_CACHE);
        Objects.requireNonNull(cacheManager().getCache(DEPOTS_CACHE)).clear();
    }

    @Scheduled(fixedRate = 1800000)
    @CacheEvict(value = DEPOT_CODE, allEntries = true)
    public void clearDepotCodeCache() {
        log.info("Cache for {} is being deleted", DEPOT_CODE);
        Objects.requireNonNull(cacheManager().getCache(DEPOT_CODE)).clear();
    }

    @Scheduled(fixedRate = 3600000)
    @CacheEvict(value = DEPOTS_CACHE, allEntries = true)
    public void clearSupplierCaches() {
        log.info("Cache for {} is being deleted", String.join(", ", SUPPLIERS_CACHE, SUPPLIER_CODE,
                SUPPLIERS_CODE, SUPPLIERS_DEPOT_CODE));
    }

}