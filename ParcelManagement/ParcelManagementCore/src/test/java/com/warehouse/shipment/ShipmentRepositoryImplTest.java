package com.warehouse.shipment;

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
import com.warehouse.shipment.configuration.ShipmentTestConfiguration;
import com.warehouse.shipment.infrastructure.adapter.secondary.ShipmentReadRepository;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.ParcelEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = ShipmentTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShipmentRepositoryImplTest {

    @Autowired
    private ShipmentReadRepository repository;

    @Test
    @DatabaseSetup("/dataset/shipment.xml")
    void shouldFindById() {
        // given
        final Long parcelId = 100001L;
        // when
        final Optional<ParcelEntity> parcel = repository.findById(parcelId);
        // then
        assertTrue(parcel.isPresent());
    }

    @Test
    @DatabaseSetup("/dataset/shipment.xml")
    void shouldNotFindById() {
        // given
        final Long parcelId = 1L;
        // when
        final Optional<ParcelEntity> parcel = repository.findById(parcelId);
        // then
        assertFalse(parcel.isPresent());
    }
}
