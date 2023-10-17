package com.warehouse.depot;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.depot.configuration.DepotTestConfiguration;
import com.warehouse.depot.infrastructure.adapter.secondary.DepotReadRepository;
import com.warehouse.depot.infrastructure.adapter.secondary.entity.DepotEntity;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = DepotTestConfiguration.class)
@TestExecutionListeners({ DependencyInjectionTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class,
        TransactionalTestExecutionListener.class,
        DbUnitTestExecutionListener.class })
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class DepotReadRepositoryTest {

    @Autowired
    private DepotReadRepository repository;

    @Test
    @DatabaseSetup("/dataset/depots.xml")
    void shouldFindDepotByCode() {
        // given
        final String depotCode = "PL1";
        // when
        final Optional<DepotEntity> depot = repository.findByDepotCode(depotCode);
        // then
        assertTrue(depot.isPresent());
        assertEquals(depotCode, depot.get().getDepotCode());
    }

    @Test
    @DatabaseSetup("/dataset/depots.xml")
    void shouldFindAll() {
        // when
        final List<DepotEntity> depot = repository.findAll();
        // then
        assertTrue(depot.size() > 0);
    }

    @Test
    @DatabaseSetup("/dataset/depots.xml")
    void shouldNotFindDepotByCode() {
        // given
        final String depotCode = "abc";
        // when
        final Optional<DepotEntity> depot = repository.findByDepotCode(depotCode);
        // then
        assertFalse(depot.isPresent());
    }
}
