package com.warehouse.shipment.infrastructure.adapter.primary;

import java.util.UUID;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.PickupPointId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.shipment.application.port.primary.ShipmentPort;
import com.warehouse.shipment.domain.enumeration.PersonType;
import com.warehouse.shipment.infrastructure.ShipmentManagementApiService;
import com.warehouse.shipment.infrastructure.adapter.primary.api.DeliveryMethodDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.DimensionsApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.LengthUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.MoneyApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PackagingTypeDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PersonApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.PickupMethodDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentCreateRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentPriorityDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentServiceLevelDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightApi;
import com.warehouse.shipment.infrastructure.adapter.primary.api.WeightUnitDto;
import com.warehouse.shipment.infrastructure.adapter.primary.exception.ShipmentValidationException;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.validator.ShipmentRequestValidator;
import com.warehouse.shipment.infrastructure.dto.ShipmentCreateApiRequestDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentCreatedDto;
import com.warehouse.shipment.infrastructure.dto.ShipmentPartyRequestDto;
import com.warehouse.shipment.application.port.secondary.ShipmentConfigurationPort;
import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.domain.exception.enumeration.ErrorCode;
import com.warehouse.shipment.domain.helper.Result;

public class ShipmentManagementApiServiceAdapter implements ShipmentManagementApiService {

    private final ShipmentPort shipmentPort;

    private final ShipmentRequestValidator shipmentRequestValidator;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentConfigurationPort shipmentConfigurationPort;

    public ShipmentManagementApiServiceAdapter(final ShipmentPort shipmentPort,
                                               final ShipmentRequestValidator shipmentRequestValidator,
                                               final ShipmentRequestMapper requestMapper,
                                               final ShipmentConfigurationPort shipmentConfigurationPort) {
        this.shipmentPort = shipmentPort;
        this.shipmentRequestValidator = shipmentRequestValidator;
        this.requestMapper = requestMapper;
        this.shipmentConfigurationPort = shipmentConfigurationPort;
    }

    @Override
    public ShipmentCreatedDto createShipment(final ShipmentCreateApiRequestDto request) {
        final ShipmentCreateRequestApi shipmentRequest = map(request);
        try {
            this.shipmentRequestValidator.validateRequest(shipmentRequest,
                    this.shipmentConfigurationPort.getCurrentOperatorShipmentConfiguration().validationRules());
        } catch (final ShipmentValidationException exception) {
            throw new IllegalArgumentException(exception.getMessage(), exception);
        }
        final Result<ShipmentCreateResponse, ErrorCode> result = this.shipmentPort.ship(this.requestMapper.mapCreateRequest(shipmentRequest));
        if (result.isFailure()) {
            throw new IllegalArgumentException(result.getFailure().getMessage());
        }
        return new ShipmentCreatedDto(new TrackingNumber(result.getSuccess().trackingNumber()));
    }

    @Override
    public void changeSender(final ShipmentId shipmentId, final ShipmentPartyRequestDto sender) {
        this.shipmentPort.changePersonTo(this.requestMapper.mapToParty(map(sender)), PersonType.SENDER, shipmentId);
    }

    @Override
    public void changeRecipient(final ShipmentId shipmentId, final ShipmentPartyRequestDto recipient) {
        this.shipmentPort.changePersonTo(this.requestMapper.mapToParty(map(recipient)), PersonType.RECIPIENT,
                shipmentId);
    }

    private ShipmentCreateRequestApi map(final ShipmentCreateApiRequestDto request) {
        if (request == null) {
            throw new IllegalArgumentException("Shipment request is required");
        }
        return new ShipmentCreateRequestApi(
                map(request.sender()),
                map(request.recipient()),
                new DimensionsApi(request.lengthCm(), request.widthCm(), request.heightCm(), LengthUnitDto.CM),
                new WeightApi(request.weightKg(), WeightUnitDto.KG),
                request.contentDescription(),
                request.declaredValue() != null ? new MoneyApi(request.declaredValue(), request.currency()) : null,
                request.customerReference(),
                new MoneyApi(request.price(), request.currency()),
                value(ShipmentPriorityDto.class, request.priority()),
                value(PackagingTypeDto.class, request.packagingType()),
                value(ShipmentServiceLevelDto.class, request.serviceLevel()),
                value(PickupMethodDto.class, request.pickupMethod()),
                value(DeliveryMethodDto.class, request.deliveryMethod()),
                pickupPointId(request.pickupPointId()),
                pickupPointId(request.deliveryPickupPointId()));
    }

    private PersonApi map(final ShipmentPartyRequestDto party) {
        if (party == null) {
            return null;
        }
        return new PersonApi(party.firstName(), party.lastName(), party.email(), party.telephoneNumber(),
                party.city(), party.postalCode(), party.street(), value(CountryCode.class, party.countryCode()));
    }

    private <E extends Enum<E>> E value(final Class<E> type, final String value) {
        return value == null || value.isBlank() ? null : Enum.valueOf(type, value.trim().toUpperCase());
    }

    private PickupPointId pickupPointId(final String value) {
        return value == null || value.isBlank() ? null : new PickupPointId(UUID.fromString(value.trim()));
    }
}
