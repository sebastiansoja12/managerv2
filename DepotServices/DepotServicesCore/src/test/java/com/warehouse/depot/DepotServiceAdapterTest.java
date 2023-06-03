package com.warehouse.depot;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.warehouse.depot.api.dto.DepotCodeDto;
import com.warehouse.depot.api.dto.DepotDto;
import com.warehouse.depot.api.dto.DepotIdDto;
import com.warehouse.depot.domain.model.Depot;
import com.warehouse.depot.domain.model.DepotCode;
import com.warehouse.depot.domain.model.DepotId;
import com.warehouse.depot.domain.port.primary.DepotPort;
import com.warehouse.depot.infrastructure.primary.DepotServiceAdapter;
import com.warehouse.depot.infrastructure.primary.mapper.DepotRequestMapper;
import com.warehouse.depot.infrastructure.primary.mapper.DepotResponseMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class DepotServiceAdapterTest {

    @Mock
    private DepotRequestMapper requestMapper;

    @Mock
    private DepotResponseMapper responseMapper;

    @Mock
    private DepotPort depotPort;

    @InjectMocks
    private DepotServiceAdapter serviceAdapter;

    private final Long DEPOT_ID = 1L;

    private final String DEPOT_CODE = "TST";

    @Test
    public void shouldAdd() {
        // Given
        final DepotDto depotDto = new DepotDto();
        final Depot depot = Depot.builder().build();
        when(requestMapper.map(depotDto)).thenReturn(depot);

        // When
        serviceAdapter.add(depotDto);

        // Then
        verify(depotPort).add(depot);
    }

    @Test
    public void shouldAddMultipleDepots() {
        // Given
        final DepotDto depotDto1 = new DepotDto();
        final DepotDto depotDto2 = new DepotDto();
        final List<DepotDto> depotsDto = Arrays.asList(depotDto1, depotDto2);

        final Depot depot1 = Depot.builder().build();
        final Depot depot2 = Depot.builder().build();
        final List<Depot> depots = Arrays.asList(depot1, depot2);
        when(requestMapper.map(depotsDto)).thenReturn(depots);

        // When
        serviceAdapter.addMultipleDepots(depotsDto);

        // Then
        verify(depotPort).addMultipleDepots(depots);
    }

    @Test
    public void shouldViewDepotById() {
        // Given
        final DepotIdDto depotIdDto = new DepotIdDto(DEPOT_ID);
        final DepotId depotId = new DepotId(DEPOT_ID);
        when(requestMapper.map(depotIdDto)).thenReturn(depotId);

        final Depot depot = Depot.builder().build();
        final DepotDto depotDto = new DepotDto();
        when(depotPort.viewDepotById(depotId)).thenReturn(depot);
        when(responseMapper.map(depot)).thenReturn(depotDto);

        // When
        final DepotDto result = serviceAdapter.viewDepotById(depotIdDto);

        // Then
        verify(depotPort).viewDepotById(depotId);
        verify(responseMapper).map(depot);
        assertEquals(depotDto, result);
    }

    @Test
    public void shouldViewDepotByCode() {
        // Given
        final DepotCodeDto depotCodeDto = new DepotCodeDto();
        final DepotCode depotCode = new DepotCode(DEPOT_CODE);
        when(requestMapper.map(depotCodeDto)).thenReturn(depotCode);

        final Depot depot = Depot.builder().build();
        final DepotDto depotDto = new DepotDto();
        when(depotPort.viewDepotByCode(depotCode)).thenReturn(depot);
        when(responseMapper.map(depot)).thenReturn(depotDto);

        // When
        final DepotDto result = serviceAdapter.viewDepotByCode(depotCodeDto);

        // Then
        verify(depotPort).viewDepotByCode(depotCode);
        verify(responseMapper).map(depot);
        assertEquals(depotDto, result);
    }

    @Test
    public void shouldFindAll() {
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
        final List<DepotDto> depotsDto = Arrays.asList(new DepotDto(), new DepotDto());
        when(depotPort.findAll()).thenReturn(depots);
        when(responseMapper.map(depots)).thenReturn(depotsDto);

        // When
        final List<DepotDto> result = serviceAdapter.findAll();

        // Then
        verify(depotPort).findAll();
        verify(responseMapper).map(depots);
        assertEquals(depotsDto.get(0).getDepotCode(), result.get(0).getDepotCode());
    }
}
