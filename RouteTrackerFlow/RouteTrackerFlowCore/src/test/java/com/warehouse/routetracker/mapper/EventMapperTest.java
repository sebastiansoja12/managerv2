package com.warehouse.routetracker.mapper;


import com.warehouse.routetracker.domain.vo.DeliveryInformation;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.EventMapper;
import com.warehouse.routetracker.infrastructure.adapter.primary.mapper.EventMapperImpl;
import com.warehouse.routetracker.infrastructure.api.dto.DeliveryInformationDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EventMapperTest {

    private EventMapper eventMapper;

    @BeforeEach
    void setup() {
        eventMapper = new EventMapperImpl();
    }

    @Test
    void shouldMapSupplyInformationDtoToSupplyInformation() {
        // given
		final DeliveryInformationDto deliveryInformationDto = new DeliveryInformationDto(1L, null, null, null, null);
        // when
        final DeliveryInformation deliveryInformation = eventMapper.map(deliveryInformationDto);
        // then
        assertThat(deliveryInformation.getParcelId()).isEqualTo(deliveryInformationDto.getParcelId());
    }
}
