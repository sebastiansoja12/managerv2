package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.primary.result.ShipmentRouteLog;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import org.mapstruct.Mapper;
import org.mapstruct.Context;
import com.warehouse.shipment.application.port.secondary.DepartmentServicePort;

import java.nio.charset.StandardCharsets;
import java.util.List;

@Mapper
public interface ShipmentResponseMapper {

    default ShipmentId map(final ShipmentIdDto shipmentId) {
        return new ShipmentId(shipmentId.getValue());
    }

    default ShipmentCreateResponseDto map(final ShipmentCreateResponse response) {
        return new ShipmentCreateResponseDto(response.shipmentId().value().toString(),
                response.trackingNumber());
    }

    default ShipmentDto map(final ShipmentSnapshot shipment, final DepartmentCode departmentCode) {
        if (shipment == null) {
            return null;
        }
        final ShipmentDto response = new ShipmentDto(
                map(shipment.shipmentId()),
                map(shipment.sender()),
                map(shipment.recipient()),
                shipment.shipmentSize() == null ? null : ShipmentSizeDto.valueOf(shipment.shipmentSize().name()),
                map(departmentCode),
                shipment.originDepartmentId(),
                shipment.pickupPointId(),
                shipment.pickupMethod() == null ? null : PickupMethodDto.valueOf(shipment.pickupMethod().name()),
                shipment.deliveryMethod() == null ? null : DeliveryMethodDto.valueOf(shipment.deliveryMethod().name()),
                shipment.originCountry(),
                shipment.destinationCountry(),
                shipment.shipmentStatus() == null ? null : ShipmentStatusDto.from(shipment.shipmentStatus()),
                map(shipment.shipmentRelatedId()),
                shipment.shipmentPriority() == null
                        ? null
                        : ShipmentPriorityDto.valueOf(shipment.shipmentPriority().name()),
                shipment.trackingNumber() == null ? null : new TrackingNumberDto(shipment.trackingNumber().value()),
                map(shipment.price()),
                shipment.locked(),
                map(shipment.signature()),
                map(shipment.dangerousGood()),
                shipment.createdAt(),
                shipment.updatedAt(),
                map(shipment.dimensions()),
                map(shipment.weight()),
                shipment.customerReference() == null ? null : shipment.customerReference().value(),
                shipment.contentDescription(),
                map(shipment.declaredValue()));
        response.setDeliveryPickupPointId(shipment.deliveryPickupPointId());
        return response;
    }

    default ShipmentDto map(final ShipmentResult shipmentResult) {
        return map(shipmentResult.snapshot(), shipmentResult.destination());
    }

    default PersonApi map(final Person person) {
        if (person == null) {
            return null;
        }
        return new PersonApi(
                person.getFirstName(),
                person.getLastName(),
                person.getEmail(),
                person.getTelephoneNumber(),
                person.getCity(),
                person.getPostalCode(),
                person.getStreet());
    }

    default DepartmentCodeDto map(final DepartmentCode departmentCode) {
        return departmentCode == null ? null : new DepartmentCodeDto(departmentCode.getValue());
    }

    default DangerousGoodApi map(final com.warehouse.shipment.domain.model.DangerousGood dangerousGood) {
        if (dangerousGood == null) {
            return null;
        }
        return new DangerousGoodApi(
                dangerousGood.getUnNumber(), dangerousGood.getProperShippingName(), dangerousGood.getDescription(),
                dangerousGood.getHazardClass(), dangerousGood.getHazardDivision(), dangerousGood.getSubsidiaryRisk(),
                dangerousGood.getPackingGroup(), dangerousGood.getQuantity(), dangerousGood.getQuantityUnit(),
                dangerousGood.getPackageCount(), dangerousGood.getPackagingType(), dangerousGood.isLimitedQuantity(),
                dangerousGood.isExceptedQuantity(), dangerousGood.isEnvironmentallyHazardous(),
                dangerousGood.isMarinePollutant(), dangerousGood.getTransportCategory(),
                dangerousGood.getTunnelRestrictionCode(), dangerousGood.getFlashPoint(),
                dangerousGood.getEmergencyContact(), dangerousGood.getEmergencyContact24h(),
                dangerousGood.getSafetyDataSheetReference(), dangerousGood.getDeclarationDocumentReference(),
                dangerousGood.getRegulationType(), dangerousGood.getTransportMode(), dangerousGood.isFlammable(),
                dangerousGood.isCorrosive(), dangerousGood.isToxic(), dangerousGood.getHazardSymbols(),
                dangerousGood.getStorageRequirements(), dangerousGood.getHandlingInstructions(),
                dangerousGood.getCountryOfOrigin()
        );
    }

    default ShipmentRouteLogResponseApi mapShipmentRouteLog(final ShipmentRouteLog shipmentRouteLog,
                                                            @Context final DepartmentServicePort departmentServicePort) {
        return new ShipmentRouteLogResponseApi(map(shipmentRouteLog.shipment()), shipmentRouteLog.routeLog(),
                shipmentRouteLog.returnPackage());
    }

    default List<String> map(String value) {
        return List.of(value);
    }

    default SignatureDto map(final Signature signature) {
        if (signature == null) {
            return null;
        }
        return new SignatureDto(signature.getSignerName(), signature.getSignedAt(), signature.getSignatureMethod().name(),
                map(signature.getSignature()));
    }

    default String map(final byte[] bytes) {
        return bytes != null ? new String(bytes, StandardCharsets.UTF_8) : null;
    }

	default MoneyApi map(final Money amount) {
		return amount == null ? null : new MoneyApi(amount.getAmount(), amount.getCurrency().name());
	}

    default DimensionsApi map(final Dimensions dimensions) {
        return dimensions == null ? null : new DimensionsApi(dimensions.length(), dimensions.width(), dimensions.height(),
                dimensions.unit() == null ? null : LengthUnitDto.valueOf(dimensions.unit().name()));
    }

    default WeightApi map(final Weight weight) {
        return weight == null ? null : new WeightApi(weight.value(),
                weight.unit() == null ? null : WeightUnitDto.valueOf(weight.unit().name()));
    }

    ShipmentUpdateResponseDto map(final ShipmentUpdateResponse response);

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        final ShipmentIdDto id;
        if (shipmentId == null) {
            id = new ShipmentIdDto();
        } else {
            id = new ShipmentIdDto(shipmentId.getValue());
        }
        return id;
    }
}
