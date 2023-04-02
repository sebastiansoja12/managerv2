package com.warehouse.reroute.infrastructure.adapter.secondary;

import com.warehouse.reroute.domain.model.UpdateParcelRequest;
import com.warehouse.reroute.domain.vo.ParcelUpdateResponse;
import com.warehouse.reroute.infrastructure.adapter.secondary.mapper.ParcelMapper;
import com.warehouse.shipment.infrastructure.api.ShipmentService;
import com.warehouse.shipment.infrastructure.api.dto.RecipientDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelRequestDto;
import com.warehouse.shipment.infrastructure.api.dto.UpdateParcelResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ParcelAdapterTest {

    @Mock
    private ShipmentService shipmentService;

    @Mock
    private ParcelMapper parcelMapper;

    @InjectMocks
    private ParcelAdapter parcelAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void shouldSendUpdateRequestToShipmentDomain() {
        // given: create test data
        final UpdateParcelRequest request = new UpdateParcelRequest();
        final UpdateParcelRequestDto requestDto = new UpdateParcelRequestDto();
        final UpdateParcelResponseDto responseDto = new UpdateParcelResponseDto();
        final ParcelUpdateResponse expectedResponse = ParcelUpdateResponse.builder().build();

        // mock mappers
        when(parcelMapper.map(request)).thenReturn(requestDto);
        when(parcelMapper.map(responseDto)).thenReturn(expectedResponse);

        /// mock service update
        when(shipmentService.update(requestDto)).thenReturn(responseDto);

        // when: call the method update in secondary adapter
        final ParcelUpdateResponse actualResponse = parcelAdapter.update(request);

        // then
        assertEquals(expectedResponse, actualResponse);

        // Verify that the parcelMapper.map() method was called exactly once with the correct argument
        verify(parcelMapper, times(1)).map(request);

        // Verify that the shipmentService.update() method was called exactly once with the correct argument
        verify(shipmentService, times(1)).update(requestDto);

        // Verify that the parcelMapper.map() method was called exactly once with the correct argument
        verify(parcelMapper, times(1)).map(responseDto);
    }

    private RecipientDto recipient() {
        return RecipientDto.builder()
                .city("Poznań").build();
    }
}
