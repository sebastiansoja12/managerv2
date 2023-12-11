package com.warehouse.routetracker;


import static org.junit.jupiter.api.Assertions.*;

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
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteDepotReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.DepotEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/db.xml")
public class DepotReadRepositoryTest {

    @Autowired
    private RouteDepotReadRepository repository;

    @Test
    void shouldFindDepotByCode() {
        // given
        final String depotCode = "TST";
        // when
        final Optional<DepotEntity> depot = repository.findByDepotCode(depotCode);
        // then
        assertTrue(depot.isPresent());
        assertEquals(depotCode, depot.get().getDepotCode());
    }

    @Test
    void shouldFindAll() {
        // when
        final List<DepotEntity> depot = repository.findAll();
        // then
        assertFalse(depot.isEmpty());
    }

    @Test
    void shouldNotFindDepotByCode() {
        // given
        final String depotCode = "abc";
        // when
        final Optional<DepotEntity> depot = repository.findByDepotCode(depotCode);
        // then
        assertFalse(depot.isPresent());
    }
}
