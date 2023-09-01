package com.warehouse.delivery;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
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
import com.warehouse.delivery.configuration.DeliveryTestConfiguration;
import com.warehouse.delivery.infrastructure.adapter.secondary.DeliveryReadRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.DeliveryEntity;

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
    void shouldFindBySupplierCode() {
        // given
        final String supplierCode = "abc_def";
        // when
        final List<DeliveryEntity> entity = repository.findBySupplierCode(supplierCode);
        // then
        assertFalse(entity.isEmpty());
    }

    @Test
    void shouldNotFindBySupplierCode() {
        // given
        final String supplierCode = "wrongCode";
        // when
        final List<DeliveryEntity> entity = repository.findBySupplierCode(supplierCode);
        // then
        assertTrue(entity.isEmpty());
    }

    @Test
    void shouldFindById() {
        // given
        final String id = "fde44928-44e5-11ee-be56-0242ac120002";
        // when
        final Optional<DeliveryEntity> entity = repository.findById(id);
        // then
        assertFalse(entity.isEmpty());
    }

    @Test
    void shouldNotFindById() {
        // given
        final String id = "fde44928-44e5-11ee-be56-0242ac120003";
        // when
        final Optional<DeliveryEntity> entity = repository.findById(id);
        // then
        assertTrue(entity.isEmpty());
    }
}
