package com.warehouse.palletmanagement.infrastructure.adapter.primary;

import com.warehouse.palletmanagement.api.dto.PalletScanRequestDto;
import com.warehouse.palletmanagement.api.dto.PalletScanResponseDto;
import com.warehouse.palletmanagement.application.port.primary.PalletScanPort;
import com.warehouse.palletmanagement.domain.exception.PalletServiceUnavailableException;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletRequestMapper;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletResponseMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PalletApiServiceAdapterTest {

    @Test
    void shouldReturnFailureResponseWhenPalletServiceUnavailable() {
        final PalletScanPort palletScanPort = mock(PalletScanPort.class);
        when(palletScanPort.scan(any())).thenThrow(new PalletServiceUnavailableException("unavailable"));
        final PalletApiServiceAdapter adapter = new PalletApiServiceAdapter(palletScanPort, new PalletRequestMapper(),
                new PalletResponseMapper());

        final PalletScanResponseDto response = adapter.scan(new PalletScanRequestDto("PAL1"));

        assertThat(response.success()).isFalse();
        assertThat(response.errorCode()).isEqualTo("PALLET_SERVICE_UNAVAILABLE");
        assertThat(response.trackingNumber()).isEqualTo("PAL1");
    }

    @Test
    void shouldReturnFailureResponseForBlankTrackingNumber() {
        final PalletApiServiceAdapter adapter = new PalletApiServiceAdapter(mock(PalletScanPort.class),
                new PalletRequestMapper(), new PalletResponseMapper());

        final PalletScanResponseDto response = adapter.scan(new PalletScanRequestDto(" "));

        assertThat(response.success()).isFalse();
        assertThat(response.errorCode()).isEqualTo("PALLET_INVALID_REQUEST");
    }
}
