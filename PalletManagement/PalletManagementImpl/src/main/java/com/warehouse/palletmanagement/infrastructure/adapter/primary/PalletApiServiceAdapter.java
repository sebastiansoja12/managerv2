package com.warehouse.palletmanagement.infrastructure.adapter.primary;

import com.warehouse.palletmanagement.api.PalletApiService;
import com.warehouse.palletmanagement.api.dto.PalletScanRequestDto;
import com.warehouse.palletmanagement.api.dto.PalletScanResponseDto;
import com.warehouse.palletmanagement.application.port.primary.PalletScanPort;
import com.warehouse.palletmanagement.domain.exception.PalletRequestRejectedException;
import com.warehouse.palletmanagement.domain.exception.PalletServiceUnavailableException;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletRequestMapper;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletResponseMapper;

public class PalletApiServiceAdapter implements PalletApiService {

    private static final String INVALID_REQUEST = "PALLET_INVALID_REQUEST";

    private final PalletScanPort palletScanPort;
    private final PalletRequestMapper palletRequestMapper;
    private final PalletResponseMapper palletResponseMapper;

    public PalletApiServiceAdapter(final PalletScanPort palletScanPort,
                                   final PalletRequestMapper palletRequestMapper,
                                   final PalletResponseMapper palletResponseMapper) {
        this.palletScanPort = palletScanPort;
        this.palletRequestMapper = palletRequestMapper;
        this.palletResponseMapper = palletResponseMapper;
    }

    @Override
    public PalletScanResponseDto scan(final PalletScanRequestDto palletScanRequest) {
        try {
            return this.palletResponseMapper.map(
                    this.palletScanPort.scan(this.palletRequestMapper.map(palletScanRequest)));
        } catch (final PalletRequestRejectedException exception) {
            return this.palletResponseMapper.failure(palletScanRequest.trackingNumber(), exception.getCode(),
                    exception.getMessage());
        } catch (final PalletServiceUnavailableException exception) {
            return this.palletResponseMapper.failure(palletScanRequest.trackingNumber(), exception.getCode(),
                    exception.getMessage());
        } catch (final IllegalArgumentException exception) {
            return this.palletResponseMapper.failure(palletScanRequest.trackingNumber(), INVALID_REQUEST,
                    exception.getMessage());
        }
    }
}
