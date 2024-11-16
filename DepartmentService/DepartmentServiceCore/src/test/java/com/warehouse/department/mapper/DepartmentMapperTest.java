package com.warehouse.department.mapper;

import com.warehouse.department.domain.model.Department;
import com.warehouse.department.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapper;
import com.warehouse.department.infrastructure.adapter.secondary.mapper.DepotMapperImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepartmentMapperTest {

    public static final String KATOWICE = "Katowice";
    private final DepotMapper mapper = new DepotMapperImpl();

    @Test
    public void shouldMapDepotToDepotEntity() {
        // Given
        final Department department = new Department();
        department.setCity(KATOWICE);
        department.setDepotCode("KT3");

        final DepotEntity expectedEntity = new DepotEntity();
        expectedEntity.setDepotCode("KT3");
        expectedEntity.setCity(KATOWICE);

        // When
        final DepotEntity result = mapper.map(department);

        // Then
        assertEquals(expectedEntity.getCity(), result.getCity());
    }

    @Test
    public void shouldMapDepotEntityToDepot() {
        // Given
        final DepotEntity depotEntity = new DepotEntity();
        depotEntity.setCity(KATOWICE);
        depotEntity.setDepotCode("KT3");


        final Department expectedDepartment = new Department();
        expectedDepartment.setCity(KATOWICE);

        // When
        final Department result = mapper.map(depotEntity);

        // Then
        assertEquals(expectedDepartment.getCity(), result.getCity());
    }

    @Test
    public void shouldMapListDepotEntityToListDepot() {
        // Given
        final List<DepotEntity> depotEntities = Arrays.asList(new DepotEntity(), new DepotEntity());

        // When
        final List<Department> result = mapper.map(depotEntities);

        // Then
        assertEquals(depotEntities.size(), result.size());
    }

    @Test
    public void shouldMapListDepotToListDepotEntity() {
        // Given
        final Department department1 = new Department();

        final Department department2 = new Department();

        final List<Department> departments = Arrays.asList(department1, department2);

        // When
        final List<DepotEntity> result = mapper.mapToDepotEntityList(departments);

        // Then
        assertEquals(departments.size(), result.size());
    }
}