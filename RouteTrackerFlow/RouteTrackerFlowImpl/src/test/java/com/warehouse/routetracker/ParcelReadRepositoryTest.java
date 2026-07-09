package com.warehouse.routetracker;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.infrastructure.adapter.secondary.ParcelReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.ParcelEntity;
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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/db.xml")
public class ParcelReadRepositoryTest {

    @Autowired
    private ParcelReadRepository repository;

    @Test
    void shouldFindById() {
        // given
        final Long parcelId = 101L;
        // when
        final Optional<ParcelEntity> parcel = repository.findById(parcelId);
        // then
        assertTrue(parcel.isPresent());
    }

    @Test
    void shouldNotFindById() {
        // given
        final Long parcelId = 11L;
        // when
        final Optional<ParcelEntity> parcel = repository.findById(parcelId);
        // then
        assertFalse(parcel.isPresent());
    }
}
