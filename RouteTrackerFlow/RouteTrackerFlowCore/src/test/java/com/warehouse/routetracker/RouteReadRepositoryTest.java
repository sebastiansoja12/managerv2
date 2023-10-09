package com.warehouse.routetracker;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

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
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.RouteEntity;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RouteReadRepositoryTest {

    @Autowired
    private RouteReadRepository repository;

    @Test
    @DatabaseSetup("/dataset/db.xml")
    void shouldFindByParcelId() {
        // given
        final Long parcelId = 100L;
        // when
        final List<RouteEntity> routeEntities = repository.findByParcelId(parcelId);
        // then
        assertNotNull(routeEntities);
    }

    @Test
    @DatabaseSetup("/dataset/db.xml")
    void shouldNotFindByParcelId() {
        // given
        final Long parcelId = 101L;
        // when
        final List<RouteEntity> routeEntities = repository.findByParcelId(parcelId);
        // then
        assertThat(routeEntities.size()).isEqualTo(0);
    }

    @Test
    @DatabaseSetup("/dataset/db.xml")
    void shouldFindByUsername() {
        // given
        final String username = "test";
        // when
        final List<RouteEntity> routeEntities = repository.findAllByUserUsername(username);
        // then
        assertNotNull(routeEntities);
    }

    @Test
    @DatabaseSetup("/dataset/db.xml")
    void shouldNotFindByUsername() {
        // given
        final String username = "test2";
        // when
        final List<RouteEntity> routeEntities = repository.findAllByUserUsername(username);
        // then
        assertThat(routeEntities.size()).isEqualTo(0);
    }
}
