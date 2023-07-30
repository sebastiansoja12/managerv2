package com.warehouse.reroute;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.warehouse.reroute.domain.service.RerouteTokenGeneratorServiceImpl;

public class RerouteTokenGeneratorServiceImplTest {
    
	private final RerouteTokenGeneratorServiceImpl
            rerouteTokenGeneratorService = new RerouteTokenGeneratorServiceImpl();


    @Test
    void shouldGenerateToken() {
        // given
        final String email = "sebastian5152@wp.pl";
        final Long parcelId = 10879952L;
        // when
        final int token = rerouteTokenGeneratorService.generate(parcelId, email);
        // then
        assertThat(token).isEqualTo(856382100);
    }

    @Test
    void shouldGenerateTokenWhenEmailIsEmpty() {
        // given
        final String email = "";
        final Long parcelId = 100001L;
        // when
        final int token = rerouteTokenGeneratorService.generate(parcelId, email);
        // then
        assertThat(token).isEqualTo(2055400);
    }
}
