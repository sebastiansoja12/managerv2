package com.warehouse.pickuppoint.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.pickuppoint.api.dto.DepartmentIdDto;
import com.warehouse.pickuppoint.api.dto.GeoCoordinatesDto;
import com.warehouse.pickuppoint.api.dto.PickupPointAddressDto;
import com.warehouse.pickuppoint.api.dto.PickupPointCapabilityDto;
import com.warehouse.pickuppoint.api.dto.PickupPointIdDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionDto;
import com.warehouse.pickuppoint.api.dto.PickupPointSelectionRequestDto;
import com.warehouse.pickuppoint.api.dto.PickupPointShipmentSizeDto;
import com.warehouse.pickuppoint.api.dto.PickupPointStatusDto;
import com.warehouse.pickuppoint.api.dto.PickupPointTypeDto;
import com.warehouse.pickuppoint.application.port.primary.result.PickupPointResult;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointCapability;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointShipmentSize;
import com.warehouse.pickuppoint.domain.enumeration.PickupPointType;
import com.warehouse.pickuppoint.domain.vo.GeoCoordinates;
import com.warehouse.pickuppoint.domain.vo.PickupPointAddress;
import com.warehouse.pickuppoint.domain.vo.PickupPointSelectionCriteria;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.util.Set;
import java.util.stream.Collectors;

public class PickupPointApiMapper {

    public PickupPointId toModel(final PickupPointIdDto pickupPointId) {
        return new PickupPointId(pickupPointId.value());
    }

    public PickupPointSelectionCriteria toModel(final PickupPointSelectionRequestDto request) {
        return new PickupPointSelectionCriteria(
                PickupPointCapability.valueOf(request.requiredCapability().name()),
                PickupPointType.valueOf(request.requiredType().name()),
                CountryCode.valueOf(request.countryCode()),
                PickupPointShipmentSize.valueOf(request.shipmentSize().name()),
                request.dangerousGoods());
    }

    public PickupPointSelectionDto toDto(final PickupPointResult result) {
        final PickupPointSnapshot pickupPoint = result.pickupPoint();
        final Set<PickupPointCapabilityDto> capabilities = pickupPoint.capabilities().stream()
                .map(capability -> PickupPointCapabilityDto.valueOf(capability.name()))
                .collect(Collectors.toSet());
        return new PickupPointSelectionDto(
                new PickupPointIdDto(pickupPoint.pickupPointId().value()),
                pickupPoint.code().value(),
                pickupPoint.name(),
                PickupPointTypeDto.valueOf(pickupPoint.type().name()),
                PickupPointStatusDto.valueOf(pickupPoint.status().name()),
                capabilities,
                address(pickupPoint.address()),
                coordinates(pickupPoint.coordinates()),
                pickupPoint.departmentId() == null ? null : new DepartmentIdDto(pickupPoint.departmentId().value()),
                pickupPoint.version(),
                result.evaluatedAt());
    }

    private PickupPointAddressDto address(final PickupPointAddress address) {
        if (address == null) {
            return null;
        }
        return new PickupPointAddressDto(
                address.countryCode().name(),
                address.postalCode(),
                address.city(),
                address.street(),
                address.buildingNumber(),
                address.unitNumber());
    }

    private GeoCoordinatesDto coordinates(final GeoCoordinates coordinates) {
        if (coordinates == null) {
            return null;
        }
        return new GeoCoordinatesDto(coordinates.latitude(), coordinates.longitude());
    }
}
