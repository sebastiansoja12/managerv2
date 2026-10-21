package com.warehouse.departmentmap.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.departmentmap.api.PalletLocationApiService;
import com.warehouse.departmentmap.api.dto.PalletLocationDto;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPort;
import com.warehouse.departmentmap.infrastructure.adapter.primary.mapper.DepartmentMapApiMapper;

import java.util.Optional;

public class PalletLocationApiServiceAdapter implements PalletLocationApiService {

    private final PalletLocationPort palletLocationPort;

    public PalletLocationApiServiceAdapter(final PalletLocationPort palletLocationPort) {
        this.palletLocationPort = palletLocationPort;
    }

    @Override
    public Optional<PalletLocationDto> findPalletLocation(final PalletTrackingNumber palletTrackingNumber) {
        return this.palletLocationPort.findPalletLocation(palletTrackingNumber).map(DepartmentMapApiMapper::toDto);
    }
}
