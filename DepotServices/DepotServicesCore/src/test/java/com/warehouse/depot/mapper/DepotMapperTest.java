package com.warehouse.depot.mapper;

import com.warehouse.depot.domain.vo.Depot;
import com.warehouse.depot.infrastructure.adapter.secondary.entity.DepotEntity;
import com.warehouse.depot.infrastructure.adapter.secondary.mapper.DepotMapper;
import com.warehouse.depot.infrastructure.adapter.secondary.mapper.DepotMapperImpl;
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
        final Depot depot = Depot.builder()
                .city(KATOWICE)
                .build();

        final DepotEntity expectedEntity = new DepotEntity();
        expectedEntity.setDepotCode("KT3");
        expectedEntity.setCity(KATOWICE);

        // When
        final DepotEntity result = mapper.map(depot);

        // Then
        assertEquals(expectedEntity.getCity(), result.getCity());
    }

    @Test
    public void shouldMapDepotEntityToDepot() {
        // Given
        final DepotEntity depotEntity = new DepotEntity();
        depotEntity.setCity("Katowice");
        depotEntity.setDepotCode("KT3");


        final Depot expectedDepot = Depot.builder()
                .city(KATOWICE)
                .build();

        // When
        final Depot result = mapper.map(depotEntity);

        // Then
        assertEquals(expectedDepot.getCity(), result.getCity());
    }

    @Test
    public void shouldMapListDepotEntityToListDepot() {
        // Given
        final List<DepotEntity> depotEntities = Arrays.asList(new DepotEntity(), new DepotEntity());

        // When
        final List<Depot> result = mapper.map(depotEntities);

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