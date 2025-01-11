package com.warehouse.department;

import com.github.springtestdbunit.DbUnitTestExecutionListener;
import com.github.springtestdbunit.annotation.DatabaseSetup;
import com.warehouse.department.configuration.DepotTestConfiguration;
import com.warehouse.department.infrastructure.adapter.secondary.DepartmentReadRepository;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepartmentEntity;
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
public class DepartmentReadRepositoryTest {

    @Autowired
    private DepartmentReadRepository repository;

    @Test
    @DatabaseSetup("/dataset/departments.xml")
    void shouldFindDepartmentByCode() {
        // given
        final String departmentCode = "PL1";
        // when
        final Optional<DepartmentEntity> department = repository.findByDepartmentCode(departmentCode);
        // then
        assertTrue(department.isPresent());
        assertEquals(departmentCode, department.get().getDepartmentCode());
    }

    @Test
    @DatabaseSetup("/dataset/departments.xml")
    void shouldFindAll() {
        // when
        final List<DepartmentEntity> depot = repository.findAll();
        // then
        assertTrue(depot.size() > 0);
    }

    @Test
    @DatabaseSetup("/dataset/departments.xml")
    void shouldNotFindDepartmentByCode() {
        // given
        final String departmentCode = "abc";
        // when
        final Optional<DepartmentEntity> department = repository.findByDepartmentCode(departmentCode);
        // then
        assertFalse(department.isPresent());
    }
}
