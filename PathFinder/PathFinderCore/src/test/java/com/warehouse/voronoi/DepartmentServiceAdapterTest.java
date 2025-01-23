package com.warehouse.voronoi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.department.domain.port.primary.DepartmentPort;
import com.warehouse.voronoi.domain.model.Department;
import com.warehouse.voronoi.infrastructure.adapter.secondary.DepotServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.mapper.DepotResponseMapper;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceAdapterTest {


    @Mock
    private DepartmentPort departmentPort;

    @Mock
    private DepotResponseMapper responseMapper;

    private DepotServiceAdapter depotServiceAdapter;

    @BeforeEach
    void setup() {
        depotServiceAdapter = new DepotServiceAdapter(departmentPort, responseMapper);
    }

    @Test
    void shouldDownloadDepots() {
        // when
        final List<Department> departmentList = depotServiceAdapter.downloadDepots();
        // then
        assertThat(departmentList).isNotNull();
    }
}
