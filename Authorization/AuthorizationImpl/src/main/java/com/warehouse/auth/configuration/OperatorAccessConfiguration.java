package com.warehouse.auth.configuration;

import com.warehouse.auth.OperatorSessionApiService;
import com.warehouse.auth.domain.port.primary.OperatorSessionPort;
import com.warehouse.auth.domain.port.primary.OperatorSessionPortImpl;
import com.warehouse.auth.domain.port.secondary.OperatorSessionRepository;
import com.warehouse.auth.domain.port.secondary.OperatorStatusServicePort;
import com.warehouse.auth.domain.service.OperatorAccessPolicy;
import com.warehouse.auth.domain.service.OperatorAccessPolicyImpl;
import com.warehouse.auth.infrastructure.adapter.primary.OperatorSessionApiServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.secondary.OperatorSessionRepositoryImpl;
import com.warehouse.auth.infrastructure.adapter.secondary.OperatorSessionRevocationReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.OperatorStatusServiceAdapter;
import com.warehouse.auth.infrastructure.adapter.secondary.RefreshTokenReadRepository;
import com.warehouse.auth.infrastructure.adapter.secondary.cache.InMemoryOperatorAccessCache;
import com.warehouse.auth.infrastructure.adapter.secondary.cache.OperatorAccessCache;
import com.warehouse.auth.infrastructure.adapter.secondary.cache.RedisOperatorAccessCache;
import com.warehouse.organisationstructure.api.OperatorApiService;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Clock;

@Configuration
public class OperatorAccessConfiguration {

    @Bean("auth.inMemoryOperatorAccessCache")
    @ConditionalOnProperty(name = "manager.redis.enabled", havingValue = "false", matchIfMissing = true)
    public OperatorAccessCache inMemoryOperatorAccessCache() {
        return new InMemoryOperatorAccessCache(Clock.systemUTC());
    }

    @Bean("auth.redisOperatorAccessCache")
    @ConditionalOnProperty(name = "manager.redis.enabled", havingValue = "true")
    public OperatorAccessCache redisOperatorAccessCache(final StringRedisTemplate stringRedisTemplate) {
        return new RedisOperatorAccessCache(stringRedisTemplate);
    }

    @Bean("auth.operatorSessionRepository")
    public OperatorSessionRepository operatorSessionRepository(
            final RefreshTokenReadRepository refreshTokenReadRepository,
            final OperatorSessionRevocationReadRepository operatorSessionRevocationReadRepository,
            final OperatorAccessCache operatorAccessCache) {
        return new OperatorSessionRepositoryImpl(refreshTokenReadRepository, operatorSessionRevocationReadRepository,
                operatorAccessCache);
    }

    @Bean("auth.operatorStatusServicePort")
    public OperatorStatusServicePort operatorStatusServicePort(
            final ObjectProvider<OperatorApiService> operatorApiService,
            final OperatorAccessCache operatorAccessCache) {
        return new OperatorStatusServiceAdapter(operatorApiService, operatorAccessCache);
    }

    @Bean
    public OperatorAccessPolicy operatorAccessPolicy(final OperatorStatusServicePort operatorStatusServicePort,
                                                     final OperatorSessionRepository operatorSessionRepository) {
        return new OperatorAccessPolicyImpl(operatorStatusServicePort, operatorSessionRepository);
    }

    @Bean
    public OperatorSessionPort operatorSessionPort(final OperatorSessionRepository operatorSessionRepository) {
        return new OperatorSessionPortImpl(operatorSessionRepository, Clock.systemUTC());
    }

    @Bean
    public OperatorSessionApiService operatorSessionApiService(final OperatorSessionPort operatorSessionPort) {
        return new OperatorSessionApiServiceAdapter(operatorSessionPort);
    }
}
