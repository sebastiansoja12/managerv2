package com.warehouse.organisationstructure.operator.domain.listener;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.event.OperatorCreatedEvent;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorDepartmentNotifyPort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorUserNotifyPort;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.service.OperatorServiceImpl;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationServiceImpl;

class OperatorDomainEventListenerTest {

    @Test
    void shouldNotifyDepartmentAndUserThenAssignRegisteringUser() {
        final InMemoryOperatorRepository operatorRepository = new InMemoryOperatorRepository();
        final InMemoryOperatorConfigurationRepository configurationRepository =
                new InMemoryOperatorConfigurationRepository();
        final OperatorService operatorService = new OperatorServiceImpl(
                operatorRepository,
                new OperatorConfigurationServiceImpl(configurationRepository)
        );
        final TestDepartmentNotifyPort departmentNotifyPort = new TestDepartmentNotifyPort();
        final TestUserNotifyPort userNotifyPort = new TestUserNotifyPort(new UserId(3333L));
        final Operator operator = OperatorTestFixtures.operator();
        operatorRepository.save(operator);
        final OperatorDomainEventListener listener =
                new OperatorDomainEventListener(departmentNotifyPort, userNotifyPort, operatorService);

        listener.handle(new OperatorCreatedEvent(operator.snapshot(), Instant.now()));

        assertEquals(1, departmentNotifyPort.snapshots.size());
        assertEquals(1, userNotifyPort.snapshots.size());
        assertEquals(new UserId(3333L), operatorRepository.findById(operator.getOperatorId())
                .orElseThrow()
                .getRegisteringUserId());
    }

    private static class TestDepartmentNotifyPort implements OperatorDepartmentNotifyPort {
        private final List<OperatorSnapshot> snapshots = new ArrayList<>();

        @Override
        public void notifyOperatorCreated(final OperatorSnapshot snapshot) {
            snapshots.add(snapshot);
        }
    }

    private static class TestUserNotifyPort implements OperatorUserNotifyPort {
        private final UserId userId;
        private final List<OperatorSnapshot> snapshots = new ArrayList<>();

        private TestUserNotifyPort(final UserId userId) {
            this.userId = userId;
        }

        @Override
        public UserId notifyOperatorCreated(final OperatorSnapshot snapshot) {
            snapshots.add(snapshot);
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
