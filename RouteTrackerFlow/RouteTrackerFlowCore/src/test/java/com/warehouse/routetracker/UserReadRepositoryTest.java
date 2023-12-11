package com.warehouse.routetracker;


import com.github.springtestdbunit.TransactionDbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.routetracker.configuration.RouteTrackerTestConfiguration;
import com.warehouse.routetracker.infrastructure.adapter.secondary.UserReadRepository;
import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.UserEntity;
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

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/db.xml")
public class UserReadRepositoryTest {

    @Autowired
    private UserReadRepository repository;

    @Test
    void shouldFindByUsername() {
        // given
        final String username = "test";
        // when
        final Optional<UserEntity> user = repository.findByUsername(username);
        // then
        assertTrue(user.isPresent());
    }

    @Test
    void shouldNotFindByUsername() {
        // given
        final String username = "fakeUser";
        // when
        final Optional<UserEntity> user = repository.findByUsername(username);
        // then
        assertTrue(user.isEmpty());
    }
}
