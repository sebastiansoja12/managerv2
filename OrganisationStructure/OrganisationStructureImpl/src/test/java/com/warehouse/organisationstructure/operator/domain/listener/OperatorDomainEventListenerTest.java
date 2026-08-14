package com.warehouse.organisationstructure.operator.domain.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.junit.jupiter.api.Test;
import org.springframework.security.core.context.SecurityContextHolder;

import com.warehouse.commonassets.context.OperatorContext;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.UsernameTenantPasswordAuthenticationToken;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.event.OperatorCreatedEvent;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorContextServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorDepartmentNotifyPort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorGeocodingConfigurationEventServicePort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorUserNotifyPort;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.service.OperatorServiceImpl;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationServiceImpl;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorContextServiceAdapter;

class OperatorDomainEventListenerTest {

    @Test
    void shouldProvisionOperatorInOperatorContext() {
        final InMemoryOperatorRepository operatorRepository = new InMemoryOperatorRepository();
        final InMemoryOperatorConfigurationRepository configurationRepository =
                new InMemoryOperatorConfigurationRepository();
        final OperatorService operatorService = new OperatorServiceImpl(
                operatorRepository,
                new OperatorConfigurationServiceImpl(configurationRepository)
        );
        final List<String> provisioningInvocations = new ArrayList<>();
        final TestDepartmentNotifyPort departmentNotifyPort = new TestDepartmentNotifyPort(provisioningInvocations);
        final TestGeocodingConfigurationEventServicePort geocodingConfigurationEventServicePort =
                new TestGeocodingConfigurationEventServicePort(provisioningInvocations);
        final TestOperatorConfigurationEventServicePort configurationEventServicePort =
                new TestOperatorConfigurationEventServicePort();
        final TestUserNotifyPort userNotifyPort = new TestUserNotifyPort(new UserId(3333L));
        final Operator operator = OperatorTestFixtures.operator();
        operatorRepository.save(operator);
        final Instant timestamp = Instant.now();
        final OperatorDomainEventListener listener =
                new OperatorDomainEventListener(
                        departmentNotifyPort,
                        geocodingConfigurationEventServicePort,
                        configurationEventServicePort,
                        userNotifyPort,
                        operatorService,
                        operatorContextServicePort()
                );

        listener.handle(new OperatorCreatedEvent(operator.snapshot(), timestamp));

        assertEquals(1, departmentNotifyPort.snapshots.size());
        assertEquals(List.of("geocoding", "department"), provisioningInvocations);
        assertEquals(1, configurationEventServicePort.snapshots.size());
        assertEquals(operator.getOperatorId(), configurationEventServicePort.operatorIds.getFirst());
        assertEquals(new UserId(3333L), configurationEventServicePort.userIds.getFirst());
        assertEquals(timestamp, configurationEventServicePort.timestamps.getFirst());
        assertEquals(1, userNotifyPort.snapshots.size());
        assertEquals(new UserId(3333L), operatorRepository.findById(operator.getOperatorId())
                .orElseThrow()
                .getRegisteringUserId());
        assertNull(SecurityContextHolder.getContext().getAuthentication());
    }

    private OperatorContextServicePort operatorContextServicePort() {
        return new OperatorContextServiceAdapter(new OperatorContext());
    }

    private static class TestDepartmentNotifyPort implements OperatorDepartmentNotifyPort {
        private final List<OperatorSnapshot> snapshots = new ArrayList<>();
        private final List<String> invocations;

        private TestDepartmentNotifyPort(final List<String> invocations) {
            this.invocations = invocations;
        }

        @Override
        public void notifyOperatorCreated(final OperatorSnapshot snapshot) {
            snapshots.add(snapshot);
            invocations.add("department");
        }
    }

    private static class TestGeocodingConfigurationEventServicePort
            implements OperatorGeocodingConfigurationEventServicePort {
        private final List<String> invocations;

        private TestGeocodingConfigurationEventServicePort(final List<String> invocations) {
            this.invocations = invocations;
        }

        @Override
        public void publishOperatorCreated(final OperatorSnapshot snapshot, final Instant timestamp) {
            invocations.add("geocoding");
        }
    }

    private static class TestOperatorConfigurationEventServicePort
            implements OperatorConfigurationEventServicePort {
        private final List<OperatorSnapshot> snapshots = new ArrayList<>();
        private final List<OperatorId> operatorIds = new ArrayList<>();
        private final List<UserId> userIds = new ArrayList<>();
        private final List<Instant> timestamps = new ArrayList<>();

        @Override
        public void publishOperatorCreated(final OperatorSnapshot snapshot, final Instant timestamp) {
            snapshots.add(snapshot);
            timestamps.add(timestamp);
            final UsernameTenantPasswordAuthenticationToken authentication =
                    (UsernameTenantPasswordAuthenticationToken) SecurityContextHolder.getContext().getAuthentication();
            operatorIds.add(authentication.getOperatorId());
            userIds.add((UserId) authentication.getPrincipal());
        }
    }

    private static class TestUserNotifyPort implements OperatorUserNotifyPort {
        private final UserId userId;
        private final List<OperatorSnapshot> snapshots = new ArrayList<>();

        private TestUserNotifyPort(final UserId userId) {
            this.userId = userId;
        }

        @Override
        public UserId notifyOperatorCreated(final OperatorSnapshot snapshot,
                                            final Consumer<UserId> beforeUserCreated) {
            snapshots.add(snapshot);
            beforeUserCreated.accept(userId);
            return userId;
        }
    }

    private static class InMemoryOperatorRepository implements OperatorRepository {
        private final Map<OperatorId, Operator> operators = new LinkedHashMap<>();

        @Override
        public List<Operator> findAll() {
            return new ArrayList<>(operators.values());
        }

        @Override
        public Optional<Operator> findById(final OperatorId operatorId) {
            return Optional.ofNullable(operators.get(operatorId));
        }

        @Override
        public Optional<Long> findMaxOperatorIdValue() {
            return operators.keySet().stream()
                    .map(OperatorId::value)
                    .max(Long::compareTo);
        }

        @Override
        public boolean existsById(final OperatorId operatorId) {
            return operators.containsKey(operatorId);
        }

        @Override
        public void save(final Operator operator) {
            operators.put(operator.getOperatorId(), operator);
        }
    }

    private static class InMemoryOperatorConfigurationRepository implements OperatorConfigurationRepository {
        private final Map<OperatorId, OperatorConfiguration> configurations = new LinkedHashMap<>();

        @Override
        public Optional<OperatorConfiguration> findByOperatorId(final OperatorId operatorId) {
            return Optional.ofNullable(configurations.get(operatorId));
        }

        @Override
        public OperatorConfiguration save(final OperatorId operatorId, final OperatorConfiguration configuration) {
            configurations.put(operatorId, configuration);
            return configuration;
        }
    }
}
