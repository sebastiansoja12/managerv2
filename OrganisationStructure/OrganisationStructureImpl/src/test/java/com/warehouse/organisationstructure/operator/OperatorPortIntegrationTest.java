package com.warehouse.organisationstructure.operator;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.listener.OperatorDomainEventListener;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.model.UpdateOperatorCommand;
import com.warehouse.organisationstructure.operator.domain.port.primary.OperatorPort;
import com.warehouse.organisationstructure.operator.domain.port.primary.OperatorPortImpl;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorDepartmentNotifyPort;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorUserNotifyPort;
import com.warehouse.organisationstructure.operator.domain.registry.DomainContext;
import com.warehouse.organisationstructure.operator.domain.service.OperatorService;
import com.warehouse.organisationstructure.operator.domain.service.OperatorServiceImpl;
import com.warehouse.organisationstructure.operator.domain.vo.OperatorSnapshot;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorReadRepository;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.OperatorRepositoryImpl;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity.OperatorEntity;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationService;
import com.warehouse.organisationstructure.operatorconfiguration.domain.service.OperatorConfigurationServiceImpl;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationReadRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationRepositoryImpl;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.OperatorConfigurationEntity;

@SpringBootTest(
        classes = OperatorPortIntegrationTest.OperatorPortIntegrationTestConfiguration.class,
        properties = {
                "spring.jpa.hibernate.ddl-auto=create-drop",
                "spring.datasource.url=jdbc:h2:mem:operator-port-it;MODE=PostgreSQL;DB_CLOSE_DELAY=-1",
                "spring.datasource.driver-class-name=org.h2.Driver"
        }
)
@Transactional
class OperatorPortIntegrationTest {

    @Autowired
    private OperatorPort operatorPort;

    @Autowired
    private OperatorRepository operatorRepository;

    @Autowired
    private OperatorConfigurationRepository operatorConfigurationRepository;

    @Autowired
    private TestDepartmentNotifyPort departmentNotifyPort;

    @Autowired
    private TestUserNotifyPort userNotifyPort;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @BeforeEach
    void setUp() {
        setEventPublisher(applicationEventPublisher);
    }

    @AfterEach
    void tearDown() {
        setEventPublisher(null);
        departmentNotifyPort.snapshots.clear();
        userNotifyPort.snapshots.clear();
    }

    @Test
    void shouldCreateOperatorAndHandleProvisioningEvent() {
        final OperatorId operatorId = operatorPort.create(OperatorTestFixtures.createCommand());

        final Operator saved = operatorRepository.findById(operatorId).orElseThrow();
        assertEquals(OperatorId.of(10000L), operatorId);
        assertEquals(new UserId(7001L), saved.getRegisteringUserId());
        assertEquals("Example Logistics", saved.getCompanyName());
        assertEquals(1, departmentNotifyPort.snapshots.size());
        assertEquals(1, userNotifyPort.snapshots.size());
        assertEquals(operatorId, departmentNotifyPort.snapshots.getFirst().operatorId());
    }

    @Test
    void shouldUpdateOperatorAndPersistConfiguration() {
        final OperatorId operatorId = operatorPort.create(OperatorTestFixtures.createCommand());
        final UpdateOperatorCommand command = OperatorTestFixtures.updateCommand();

        final Operator updated = operatorPort.update(operatorId, command);

        assertEquals("Updated Logistics", updated.getCompanyName());
        assertEquals(command.status(), updated.getStatus());
        assertTrue(operatorConfigurationRepository.findByOperatorId(operatorId).isPresent());
        assertFalse(operatorConfigurationRepository.findByOperatorId(operatorId)
                .orElseThrow()
                .getShippingCapabilities()
                .isSupportsInternationalShipping());
    }

    private void setEventPublisher(final ApplicationEventPublisher publisher) {
        try {
            final Field field = DomainContext.class.getDeclaredField("eventPublisher");
            field.setAccessible(true);
            field.set(null, publisher);
        } catch (final ReflectiveOperationException exception) {
            throw new IllegalStateException(exception);
        }
    }

    @SpringBootConfiguration
    @EnableAutoConfiguration
    @EnableJpaRepositories(basePackageClasses = {
            OperatorReadRepository.class,
            OperatorConfigurationReadRepository.class
    })
    @EntityScan(basePackageClasses = {
            OperatorEntity.class,
            OperatorConfigurationEntity.class
    })
    static class OperatorPortIntegrationTestConfiguration {

        @Bean
        DomainContext domainContext() {
            return new DomainContext();
        }

        @Bean
        OperatorRepository operatorRepository(final OperatorReadRepository operatorReadRepository) {
            return new OperatorRepositoryImpl(operatorReadRepository);
        }

        @Bean
        OperatorConfigurationRepository operatorConfigurationRepository(
                final OperatorConfigurationReadRepository readRepository) {
            return new OperatorConfigurationRepositoryImpl(readRepository);
        }

        @Bean
        OperatorConfigurationService operatorConfigurationService(
                final OperatorConfigurationRepository operatorConfigurationRepository) {
            return new OperatorConfigurationServiceImpl(operatorConfigurationRepository);
        }

        @Bean
        OperatorService operatorService(final OperatorRepository operatorRepository,
                                        final OperatorConfigurationService operatorConfigurationService) {
            return new OperatorServiceImpl(operatorRepository, operatorConfigurationService);
        }

        @Bean
        OperatorPort operatorPort(final OperatorService operatorService) {
            return new OperatorPortImpl(operatorService);
        }

        @Bean
        TestDepartmentNotifyPort testDepartmentNotifyPort() {
            return new TestDepartmentNotifyPort();
        }

        @Bean
        TestUserNotifyPort testUserNotifyPort() {
            return new TestUserNotifyPort();
        }

        @Bean
        OperatorDomainEventListener operatorDomainEventListener(
                final TestDepartmentNotifyPort departmentNotifyPort,
                final TestUserNotifyPort userNotifyPort,
                final OperatorService operatorService) {
            return new OperatorDomainEventListener(departmentNotifyPort, userNotifyPort, operatorService);
        }
    }

    static class TestDepartmentNotifyPort implements OperatorDepartmentNotifyPort {
        private final List<OperatorSnapshot> snapshots = new ArrayList<>();

        @Override
        public void notifyOperatorCreated(final OperatorSnapshot snapshot) {
            snapshots.add(snapshot);
        }
    }

    static class TestUserNotifyPort implements OperatorUserNotifyPort {
        private final List<OperatorSnapshot> snapshots = new ArrayList<>();

        @Override
        public UserId notifyOperatorCreated(final OperatorSnapshot snapshot) {
            snapshots.add(snapshot);
            return new UserId(7001L);
        }
    }
}
