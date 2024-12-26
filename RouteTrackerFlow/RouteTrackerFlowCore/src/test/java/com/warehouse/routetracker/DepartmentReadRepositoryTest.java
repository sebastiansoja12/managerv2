package com.warehouse.routetracker;


import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import com.warehouse.routetracker.infrastructure.adapter.secondary.entity.DepartmentEntity;
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
import com.warehouse.routetracker.infrastructure.adapter.secondary.RouteDepartmentReadRepository;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ContextConfiguration(classes = RouteTrackerTestConfiguration.class)
@TestExecutionListeners({DependencyInjectionTestExecutionListener.class, TransactionDbUnitTestExecutionListener.class})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@DatabaseSetup("/dataset/db.xml")
public class DepartmentReadRepositoryTest {

    @Autowired
    private RouteDepartmentReadRepository repository;

    @Test
    void shouldFindDepartmentByCode() {
        // given
        final String departmentCode = "TST";
        // when
        final Optional<DepartmentEntity> department = repository.findByDepartmentCode(departmentCode);
        // then
        assertTrue(department.isPresent());
        assertEquals(departmentCode, department.get().getDepartmentCode());
    }

    @Test
    void shouldFindAll() {
        // when
        final List<DepartmentEntity> departments = repository.findAll();
        // then
        assertFalse(departments.isEmpty());
    }

    @Test
    void shouldNotFindDepartmentByCode() {
        // given
        final String departmentCode = "abc";
        // when
        final Optional<DepartmentEntity> department = repository.findByDepartmentCode(departmentCode);
        // then
        assertFalse(department.isPresent());
    }
}
