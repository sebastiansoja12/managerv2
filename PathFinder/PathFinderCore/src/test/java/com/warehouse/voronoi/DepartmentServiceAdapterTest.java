package com.warehouse.voronoi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.department.api.DepartmentApiService;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.infrastructure.adapter.secondary.DepartmentServiceAdapter;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceAdapterTest {


    @Mock
    private DepartmentApiService departmentApiService;

    private DepartmentServiceAdapter depotServiceAdapter;

    @BeforeEach
    void setup() {
        depotServiceAdapter = new DepartmentServiceAdapter(departmentApiService);
    }

    @Test
    void shouldDownloadDepartments() {
        // when
        final List<Department> departmentList = depotServiceAdapter.downloadDepartments();
        // then
        assertThat(departmentList).isNotNull();
    }
}
