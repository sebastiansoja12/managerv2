package com.warehouse.depot.mapper;

import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.infrastructure.secondary.entity.DepotEntity;
import com.warehouse.depot.infrastructure.secondary.mapper.DepotMapper;
import com.warehouse.depot.infrastructure.secondary.mapper.DepotMapperImpl;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DepotMapperTest {

    public static final String KATOWICE = "Katowice";
    private final DepotMapper mapper = new DepotMapperImpl();

    @Test
    public void shouldMapDepotToDepotEntity() {
        // Given
        Depot depot = Depot.builder()
                .city(KATOWICE)
                .build();

        DepotEntity expectedEntity = new DepotEntity();
        expectedEntity.setId(1L);
        expectedEntity.setCity(KATOWICE);
        expectedEntity.setLon(10.0);
        expectedEntity.setLat(20.0);

        // When
        DepotEntity result = mapper.map(depot);

        // Then
        assertEquals(expectedEntity.getCity(), result.getCity());
    }

    @Test
    public void shouldMapDepotEntityToDepot() {
        // Given
        DepotEntity depotEntity = new DepotEntity();
        depotEntity.setCity("Katowice");
        depotEntity.setId(1L);
        depotEntity.setLon(10.0);
        depotEntity.setLat(20.0);


        Depot expectedDepot = Depot.builder()
                .city(KATOWICE)
                .build();

        // When
        Depot result = mapper.map(depotEntity);

        // Then
        assertEquals(expectedDepot.getCity(), result.getCity());
    }

    @Test
    public void shouldMapListDepotEntityToListDepot() {
        // Given
        List<DepotEntity> depotEntities = Arrays.asList(new DepotEntity(), new DepotEntity());

        // When
        List<Depot> result = mapper.map(depotEntities);

        // Then
        assertEquals(depotEntities.size(), result.size());
    }

    @Test
    public void shouldMapListDepotToListDepotEntity() {
        // Given
        final Depot depot1 = Depot.builder()
                .depotCode("KT1")
                .city("Gliwice")
                .street("Mrągowska 11")
                .country("Poland")
                .build();

        final Depot depot2 = Depot.builder()
                .depotCode("POZ")
                .city("Poznań")
                .street("Wielkopolska 11")
                .country("Poland")
                .build();

        final List<Depot> depots = Arrays.asList(depot1, depot2);

        // When
        final List<DepotEntity> result = mapper.mapToDepotEntityList(depots);

        // Then
        assertEquals(depots.size(), result.size());
    }
}