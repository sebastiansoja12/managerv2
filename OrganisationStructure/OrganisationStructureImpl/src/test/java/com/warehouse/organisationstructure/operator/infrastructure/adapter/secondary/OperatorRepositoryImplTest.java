package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.OperatorTestFixtures;
import com.warehouse.organisationstructure.operator.domain.model.Operator;
import com.warehouse.organisationstructure.operator.domain.port.secondary.OperatorRepository;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity.OperatorEntity;
import com.warehouse.organisationstructure.operatorconfiguration.domain.model.OperatorConfiguration;
import com.warehouse.organisationstructure.operatorconfiguration.domain.port.secondary.OperatorConfigurationRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationReadRepository;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.OperatorConfigurationRepositoryImpl;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.OperatorConfigurationEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = "spring.jpa.hibernate.ddl-auto=create-drop")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = OperatorRepositoryImplTest.JpaTestConfiguration.class)
class OperatorRepositoryImplTest {

    @Autowired
    private OperatorReadRepository operatorReadRepository;

    @Autowired
    private OperatorConfigurationReadRepository operatorConfigurationReadRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void shouldSaveAndReadOperatorUsingRealJpaRepository() {
        final OperatorRepository repository = new OperatorRepositoryImpl(operatorReadRepository);
        final Operator operator = OperatorTestFixtures.operator();

        repository.save(operator);
        flushAndClear();

        final Operator saved = repository.findById(operator.getOperatorId()).orElseThrow();
        assertEquals(operator.getOperatorId(), saved.getOperatorId());
        assertEquals(operator.getTaxId(), saved.getTaxId());
        assertEquals(operator.getCompanyName(), saved.getCompanyName());
        assertTrue(repository.existsById(operator.getOperatorId()));
        assertEquals(operator.getOperatorId().value(), repository.findMaxOperatorIdValue().orElseThrow());
    }

    @Test
    void shouldReturnEmptyWhenOperatorDoesNotExist() {
        final OperatorRepository repository = new OperatorRepositoryImpl(operatorReadRepository);

        assertFalse(repository.findById(OperatorId.of(99999L)).isPresent());
        assertFalse(repository.existsById(OperatorId.of(99999L)));
    }

    @Test
    void shouldSaveAndReadOperatorConfigurationUsingRealJpaRepository() {
        final OperatorConfigurationRepository repository =
                new OperatorConfigurationRepositoryImpl(operatorConfigurationReadRepository);

        repository.save(OperatorTestFixtures.OPERATOR_ID, OperatorTestFixtures.configuration());
        flushAndClear();

        final OperatorConfiguration saved = repository.findByOperatorId(OperatorTestFixtures.OPERATOR_ID)
                .orElseThrow();
        assertTrue(saved.getShippingCapabilities().isSupportsInternationalShipping());
        assertEquals(31.5, saved.getShipmentLimits().getMaxWeight());
        assertEquals(7, saved.getDeliveryTimeConfiguration().getInternationalDeliveryDays());
    }

    private void flushAndClear() {
        entityManager.flush();
        entityManager.clear();
    }

    @EnableJpaRepositories(basePackageClasses = {
            OperatorReadRepository.class,
            OperatorConfigurationReadRepository.class
    })
    @EntityScan(basePackageClasses = {
            OperatorEntity.class,
            OperatorConfigurationEntity.class
    })
    static class JpaTestConfiguration {
    }
}
