package com.warehouse.voronoi;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.voronoi.domain.model.Depot;
import com.warehouse.voronoi.infrastructure.adapter.secondary.DepotServiceAdapter;
import com.warehouse.voronoi.infrastructure.adapter.secondary.mapper.DepotResponseMapper;

@ExtendWith(MockitoExtension.class)
public class DepotServiceAdapterTest {


    @Mock
    private DepotPort depotPort;

    @Mock
    private DepotResponseMapper responseMapper;

    private DepotServiceAdapter depotServiceAdapter;

    @BeforeEach
    void setup() {
        depotServiceAdapter = new DepotServiceAdapter(depotPort, responseMapper);
    }

    @Test
    void shouldDownloadDepots() {
        // when
        final List<Depot> depotList = depotServiceAdapter.downloadDepots();
        // then
        assertThat(depotList).isNotNull();
    }
}
