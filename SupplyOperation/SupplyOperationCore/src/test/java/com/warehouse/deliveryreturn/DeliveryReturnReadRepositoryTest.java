package com.warehouse.deliveryreturn;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

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

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.DeliveryReturnReadRepository;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.entity.DeliveryReturnEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryReturnReadRepositoryTest.DeliveryRepositoryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/delivery.xml")
public class DeliveryReturnReadRepositoryTest {

    @EntityScan(basePackages = { "com.warehouse.deliveryreturn" })
    @EnableJpaRepositories(basePackages = { "com.warehouse.deliveryreturn" })
    public static class DeliveryRepositoryTestConfiguration {

    }

    @Autowired
    private DeliveryReturnReadRepository repository;

    @Test
    void shouldFindById() {
        // given
        final String uuid = "fde44928-44e5-11ee-be56-0242ac120002";
        // when
        final Optional<DeliveryReturnEntity> deliveryReturnEntity = repository.findById(uuid);
        // then
        assertTrue(deliveryReturnEntity.isPresent());
    }

    @Test
    void shouldNotFindById() {
        // given
        final String uuid = "fde44928-44e5-11ee-be56-0242ac120003";
        // when
        final Optional<DeliveryReturnEntity> deliveryReturnEntity = repository.findById(uuid);
        // then
        assertTrue(deliveryReturnEntity.isEmpty());
    }
}

