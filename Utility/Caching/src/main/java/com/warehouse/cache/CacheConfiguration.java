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


    private static final String DEPARTMENT_CACHE = "departmentsCache";

    private static final String DEPARTMENT_CODE = "departmentCodeCache";

    private static final String SUPPLIERS_CACHE = "suppliersCache";

    private static final String SUPPLIER_CODE = "supplierCodeCache";

    private static final String SUPPLIERS_CODE = "suppliersCodeCache";

    private static final String SUPPLIERS_DEPARTMENT_CODE = "suppliersDepartmentCodeCache";

    @Bean
    public CacheManager cacheManager() {
        return new ConcurrentMapCacheManager(DEPARTMENT_CACHE, DEPARTMENT_CODE, SUPPLIERS_CACHE, SUPPLIER_CODE,
                SUPPLIERS_CODE, SUPPLIERS_DEPARTMENT_CODE);
    }

    @Scheduled(fixedRate = 3600000)
    @CacheEvict(value = DEPARTMENT_CACHE, allEntries = true)
    public void clearDepartmentCache() {
        log.info("Cache for {} is being deleted", DEPARTMENT_CACHE);
        Objects.requireNonNull(cacheManager().getCache(DEPARTMENT_CACHE)).clear();
    }

    @Scheduled(fixedRate = 1800000)
    @CacheEvict(value = DEPARTMENT_CODE, allEntries = true)
    public void clearDepartmentCodeCache() {
        log.info("Cache for {} is being deleted", DEPARTMENT_CODE);
        Objects.requireNonNull(cacheManager().getCache(DEPARTMENT_CODE)).clear();
    }

    @Scheduled(fixedRate = 3600000)
    @CacheEvict(value = {SUPPLIERS_CACHE, SUPPLIER_CODE, SUPPLIERS_CODE, SUPPLIERS_DEPARTMENT_CODE}, allEntries = true)
    public void clearSupplierCaches() {
        log.info("Cache for {} is being deleted", String.join(", ", SUPPLIERS_CACHE, SUPPLIER_CODE,
                SUPPLIERS_CODE, SUPPLIERS_DEPARTMENT_CODE));
    }

}