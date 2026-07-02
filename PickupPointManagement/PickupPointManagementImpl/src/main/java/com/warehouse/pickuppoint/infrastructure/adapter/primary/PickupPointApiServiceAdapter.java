package com.warehouse.pickuppoint.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.api.PickupPointApiService;
import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionRequestDto;
import com.warehouse.pickuppoint.application.port.primary.PickupPointPort;
import com.warehouse.pickuppoint.infrastructure.adapter.primary.mapper.PickupPointApiMapper;

import java.util.List;
import java.util.Optional;

public class PickupPointApiServiceAdapter implements PickupPointApiService {

    private final PickupPointPort pickupPointPort;
    private final PickupPointApiMapper pickupPointApiMapper;

    public PickupPointApiServiceAdapter(
            final PickupPointPort pickupPointPort,
            final PickupPointApiMapper pickupPointApiMapper) {
        this.pickupPointPort = pickupPointPort;
        this.pickupPointApiMapper = pickupPointApiMapper;
    }

    @Override
    public Optional<PickupPointSelectionDto> getById(final PickupPointIdDto pickupPointId) {
        return this.pickupPointPort.get(this.pickupPointApiMapper.toModel(pickupPointId))
                .map(this.pickupPointApiMapper::toDto);
    }

    @Override
    public List<PickupPointSelectionDto> getByIds(final List<PickupPointIdDto> pickupPointIds) {
        final List<PickupPointId> identifiers = pickupPointIds.stream()
                .map(this.pickupPointApiMapper::toModel)
                .toList();
        return this.pickupPointPort.get(identifiers).stream()
                .map(this.pickupPointApiMapper::toDto)
                .toList();
    }

    @Override
    public PickupPointSelectionDto validateSelection(final PickupPointSelectionRequestDto selectionRequest) {
        return this.pickupPointApiMapper.toDto(this.pickupPointPort.validateSelection(
                this.pickupPointApiMapper.toModel(selectionRequest.pickupPointId()),
                this.pickupPointApiMapper.toModel(selectionRequest)));
    }
}
