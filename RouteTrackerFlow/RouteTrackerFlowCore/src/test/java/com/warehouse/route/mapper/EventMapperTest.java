package com.warehouse.route.mapper;


import com.warehouse.route.domain.vo.DeliveryInformation;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapper;
import com.warehouse.route.infrastructure.adapter.primary.mapper.EventMapperImpl;
import com.warehouse.route.infrastructure.api.dto.DeliveryInformationDto;
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
        final DeliveryInformationDto deliveryInformationDto = new DeliveryInformationDto();
        deliveryInformationDto.setParcelId(1L);
        // when
        final DeliveryInformation deliveryInformation = eventMapper.map(deliveryInformationDto);
        // then
        assertThat(deliveryInformation.getParcelId()).isEqualTo(deliveryInformationDto.getParcelId());
    }
}
