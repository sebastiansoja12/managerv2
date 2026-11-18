package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.cache;

import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.DeserializationFeature;
import tools.jackson.databind.json.JsonMapper;

import java.time.Duration;
import java.util.Optional;

@Slf4j
public class RedisOperatorConfigurationCache implements OperatorConfigurationCache {

    static final Duration CONFIGURATION_TTL = Duration.ofMinutes(10);

    static final JsonMapper JSON_MAPPER = JsonMapper.builder()
            .changeDefaultVisibility(visibility -> visibility
                    .withFieldVisibility(Visibility.ANY)
                    .withGetterVisibility(Visibility.NONE)
                    .withIsGetterVisibility(Visibility.NONE)
                    .withSetterVisibility(Visibility.NONE)
                    .withCreatorVisibility(Visibility.ANY))
            .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
            .build();

    private final StringRedisTemplate redisTemplate;

    public RedisOperatorConfigurationCache(final StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Optional<OperatorConfiguration> find(final OperatorId operatorId) {
        final String key = key(operatorId);
        try {
            return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                    .map(value -> JSON_MAPPER.readValue(value, OperatorConfiguration.class));
        } catch (final JacksonException exception) {
            log.warn("Cached operator configuration {} could not be read, evicting it: {}", key,
                    exception.getMessage());
            evict(operatorId);
            return Optional.empty();
        } catch (final DataAccessException exception) {
            log.warn("Redis operator configuration cache read failed, falling back to database: {}",
                    exception.getMessage());
            return Optional.empty();
        }
    }

    @Override
    public void put(final OperatorId operatorId, final OperatorConfiguration configuration) {
        try {
            redisTemplate.opsForValue().set(key(operatorId), JSON_MAPPER.writeValueAsString(configuration),
                    CONFIGURATION_TTL);
        } catch (final JacksonException | DataAccessException exception) {
            log.warn("Redis operator configuration cache write failed: {}", exception.getMessage());
        }
    }

    @Override
    public void evict(final OperatorId operatorId) {
        delete(operatorId);
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCompletion(final int status) {
                    delete(operatorId);
                }
            });
        }
    }

    static String key(final OperatorId operatorId) {
        return "organisation:operator:" + operatorId.value() + ":configuration:v1";
    }

    private void delete(final OperatorId operatorId) {
        try {
            redisTemplate.delete(key(operatorId));
        } catch (final DataAccessException exception) {
            log.warn("Redis operator configuration cache eviction failed: {}", exception.getMessage());
        }
    }
}
