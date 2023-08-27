package com.warehouse.delivery;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.delivery.configuration.DeliveryTestConfiguration;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryReadRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/delivery.xml")
public class DeliveryReadRepositoryTest {

    @Autowired
    private DeliveryReadRepository repository;


    @Test
    void shouldFindById() {
        // given
        final UUID id = UUID.fromString("fde44928-44e5-11ee-be56-0242ac120002");
        // when
        final Optional<DeliveryEntity> entity = repository.findById(id);
        // then
        assertTrue(entity.isPresent());
    }
}
