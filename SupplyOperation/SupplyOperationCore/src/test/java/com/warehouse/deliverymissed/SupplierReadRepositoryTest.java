package com.warehouse.deliverymissed;

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
import com.warehouse.deliverymissed.configuration.DeliveryMissedTestConfiguration;
import com.warehouse.delivery.infrastructure.adapter.secondary.SupplierReadRepository;
import com.warehouse.delivery.infrastructure.adapter.secondary.entity.SupplierEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DeliveryMissedTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/supplier.xml")
public class SupplierReadRepositoryTest {

    @Autowired
    private SupplierReadRepository repository;

    @Test
    void shouldReturnSupplierByCode() {
        // given
        final String supplierCode = "abcdef";
        // when
        final Optional<SupplierEntity> supplier = repository.findBySupplierCode(supplierCode);
        // then
        assertTrue(supplier.isPresent());
    }

    @Test
    void shouldNotReturnSupplierByCode() {
        // given
        final String supplierCode = "fedcba";
        // when
        final Optional<SupplierEntity> supplier = repository.findBySupplierCode(supplierCode);
        // then
        assertFalse(supplier.isPresent());
    }
}
