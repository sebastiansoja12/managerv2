package com.warehouse.deliverymissed;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.DeliveryMissedReadRepository;
import com.warehouse.deliverymissed.infrastructure.adapter.secondary.entity.DeliveryMissedEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryMissedReadRepositoryTest.DeliveryMissedRepositoryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/deliverymissed/repository/deliverymissed.xml")
public class DeliveryMissedReadRepositoryTest {

    @EntityScan(basePackages = { "com.warehouse.deliverymissed" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.deliverymissed" })
    public static class DeliveryMissedRepositoryTestConfiguration {

    }

    @Autowired
    private DeliveryMissedReadRepository repository;

    @Test
    void shouldFindById() {
        // given
        final String uuid = "fde44928-44e5-11ee-be56-0242ac120002";
        // when
        final Optional<DeliveryMissedEntity> deliveryReturnEntity = repository.findById(uuid);
        // then
        assertTrue(deliveryReturnEntity.isPresent());
    }

    @Test
    void shouldNotFindById() {
        // given
        final String uuid = "fde44928-44e5-11ee-be56-0242ac120003";
        // when
        final Optional<DeliveryMissedEntity> deliveryReturnEntity = repository.findById(uuid);
        // then
        assertTrue(deliveryReturnEntity.isEmpty());
    }
}

