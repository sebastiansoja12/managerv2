package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;
import com.warehouse.shipment.application.port.primary.result.ShipmentControlCenterResult;
import com.warehouse.shipment.application.port.primary.result.ShipmentResult;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.Signature;
import com.warehouse.shipment.domain.vo.*;
import com.warehouse.shipment.infrastructure.adapter.primary.api.*;
import org.mapstruct.Mapper;

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

    default ShipmentReturnDetailsApi map(final ShipmentReturnDetails response) {
        return new ShipmentReturnDetailsApi(
                new ShipmentReturnDetailsApi.LongValueApi(response.returnPackageId().getId()),
                map(response.shipmentId()),
                response.reason(),
                response.returnStatus(),
                response.returnToken() == null
                        ? null
                        : new ShipmentReturnDetailsApi.StringValueApi(response.returnToken()),
                response.assignedDepartmentCode() == null
                        ? null
                        : new DepartmentCodeDto(response.assignedDepartmentCode().value()),
                response.returnedDepartmentCode() == null
                        ? null
                        : new DepartmentCodeDto(response.returnedDepartmentCode().value()),
                response.assignedTo() == null
                        ? null
                        : new ShipmentReturnDetailsApi.LongValueApi(response.assignedTo().value()),
                response.processedBy() == null
                        ? null
                        : new ShipmentReturnDetailsApi.LongValueApi(response.processedBy().value()),
                response.reasonCode() == null
                        ? null
                        : new ShipmentReturnDetailsApi.StringValueApi(response.reasonCode().name()),
                response.operatorId(),
                response.createdAt(),
                response.updatedAt());
    }

    default ShipmentReturnPageApi map(final ShipmentReturnPage response) {
        return new ShipmentReturnPageApi(
                response.content().stream().map(this::map).toList(),
                response.page(),
                response.size(),
                response.totalElements(),
                response.totalPages());
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
                shipment.updatedAt());
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

    default ShipmentControlCenterResponseApi mapControlCenter(final ShipmentControlCenterResult controlCenter) {
        return new ShipmentControlCenterResponseApi(map(controlCenter.shipment()), controlCenter.routeLog(),
                controlCenter.returnPackage() == null ? null : map(controlCenter.returnPackage()));
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
