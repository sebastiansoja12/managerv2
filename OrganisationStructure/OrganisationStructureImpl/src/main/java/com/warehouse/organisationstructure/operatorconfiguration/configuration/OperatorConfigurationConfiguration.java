package com.warehouse.organisationstructure.operatorconfiguration.configuration;

import com.warehouse.auth.CurrentOperatorService;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPort;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.primary.OperatorConfigurationPortImpl;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationServiceImpl;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.primary.OperatorConfigurationServiceAdapter;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationReadRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationRepositoryImpl;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache.NoOpOperatorConfigurationCache;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache.OperatorConfigurationCache;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache.RedisOperatorConfigurationCache;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
public class OperatorConfigurationConfiguration {

    @Bean("organisationstructure.noOpOperatorConfigurationCache")
    @ConditionalOnProperty(name = "manager.redis.enabled", havingValue = "false", matchIfMissing = true)
    public OperatorConfigurationCache noOpOperatorConfigurationCache() {
        return new NoOpOperatorConfigurationCache();
    }

    @Bean("organisationstructure.redisOperatorConfigurationCache")
    @ConditionalOnProperty(name = "manager.redis.enabled", havingValue = "true")
    public OperatorConfigurationCache redisOperatorConfigurationCache(final StringRedisTemplate stringRedisTemplate) {
        return new RedisOperatorConfigurationCache(stringRedisTemplate);
    }

    @Bean
    public OperatorConfigurationRepository operatorConfigurationRepository(
            final OperatorConfigurationReadRepository operatorConfigurationReadRepository,
            final OperatorConfigurationCache operatorConfigurationCache) {
        return new OperatorConfigurationRepositoryImpl(operatorConfigurationReadRepository,
                operatorConfigurationCache);
    }

    @Bean
    public OperatorConfigurationService operatorConfigurationService(
            final OperatorConfigurationRepository operatorConfigurationRepository) {
        return new OperatorConfigurationServiceImpl(operatorConfigurationRepository);
    }

    @Bean
    public OperatorConfigurationPort operatorConfigurationPort(
            final OperatorConfigurationService operatorConfigurationService,
            final CurrentOperatorService currentOperatorService) {
        return new OperatorConfigurationPortImpl(operatorConfigurationService, currentOperatorService);
    }

    @Bean
    public OperatorConfigurationServiceAdapter operatorConfigurationApiService(
            final OperatorConfigurationPort operatorConfigurationPort) {
        return new OperatorConfigurationServiceAdapter(operatorConfigurationPort);
    }
}
