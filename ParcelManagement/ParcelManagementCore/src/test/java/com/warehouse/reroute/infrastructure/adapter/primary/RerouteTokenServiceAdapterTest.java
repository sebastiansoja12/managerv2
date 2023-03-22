package com.warehouse.reroute.infrastructure.adapter.primary;

import com.warehouse.reroute.domain.port.primary.RerouteServicePort;
import com.warehouse.reroute.domain.port.primary.RerouteServicePortImpl;
import com.warehouse.reroute.domain.service.RerouteService;
import com.warehouse.reroute.domain.service.RerouteTokenValidatorService;
import com.warehouse.reroute.domain.service.RerouteTokenValidatorServiceImpl;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.PrimaryRequestMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.PrimaryRequestMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.PrimaryResponseMapper;
import com.warehouse.reroute.infrastructure.adapter.primary.mapper.PrimaryResponseMapperImpl;
import com.warehouse.reroute.infrastructure.adapter.secondary.ParcelShipmentReadRepository;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;
import com.warehouse.reroute.infrastructure.api.dto.EmailDto;
import com.warehouse.reroute.infrastructure.api.dto.ParcelId;
import com.warehouse.reroute.infrastructure.api.dto.RerouteRequestDto;
import com.warehouse.reroute.infrastructure.api.dto.RerouteResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class RerouteTokenServiceAdapterTest {



    private RerouteTokenServiceAdapter adapter;

    @Mock
    private ParcelShipmentReadRepository parcelShipmentReadRepository;

    @Mock
    private RerouteTokenReadRepository rerouteTokenReadRepository;

    @Mock
    private RerouteService rerouteService;


    @BeforeEach
    void setUp() {
        final PrimaryRequestMapper requestMapper = new PrimaryRequestMapperImpl();
        final PrimaryResponseMapper responseMapper = new PrimaryResponseMapperImpl();
        final RerouteTokenValidatorService rerouteTokenValidatorService =
                new RerouteTokenValidatorServiceImpl(rerouteTokenReadRepository);
        final RerouteServicePort port = new RerouteServicePortImpl(rerouteService, rerouteTokenValidatorService);
        adapter = new RerouteTokenServiceAdapter(port, requestMapper, responseMapper);
    }

    @Test
    void shouldNotSendReroutingInformation() {
        // given
        final RerouteRequestDto requestDto = new RerouteRequestDto();
        requestDto.setEmail(emailDto());
        requestDto.setParcelId(parcelId());

        // when
        final RerouteResponseDto responseDto = adapter.sendReroutingInformation(requestDto);
        // then
        assertThat(responseDto).isNull();

    }

    private EmailDto emailDto() {
        final EmailDto email = new EmailDto();
        email.setValue("sebastian5152@wp.pl");
        return email;
    }

    private ParcelId parcelId() {
        final ParcelId parcelId = new ParcelId();
        parcelId.setValue(123456L);
        return parcelId;
    }
}
