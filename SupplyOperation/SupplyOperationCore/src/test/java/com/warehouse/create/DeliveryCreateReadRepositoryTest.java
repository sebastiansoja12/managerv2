package com.warehouse.create;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.create.configuration.DeliveryCreateTestConfiguration;
import com.warehouse.create.infrastructure.adapter.secondary.DeliveryCreateReadRepository;
import com.warehouse.create.infrastructure.adapter.secondary.entity.DeliveryCreateEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryCreateTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/delivery.xml")
public class DeliveryCreateReadRepositoryTest {

    @Autowired
    private DeliveryCreateReadRepository repository;


    @Test
    void shouldFindById() {
        // given
        final String id = "fde44928-44e5-11ee-be56-0242ac120002";
        // when
        final Optional<DeliveryCreateEntity> entity = repository.findById(id);
        // then
        assertFalse(entity.isEmpty());
    }

    @Test
    void shouldNotFindById() {
        // given
        final String id = "fde44928-44e5-11ee-be56-0242ac120003";
        // when
        final Optional<DeliveryCreateEntity> entity = repository.findById(id);
        // then
        assertTrue(entity.isEmpty());
    }
}
