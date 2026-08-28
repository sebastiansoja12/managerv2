package com.warehouse.shipment.infrastructure.adapter.primary.mapper;

import com.warehouse.shipment.application.port.primary.result.ShipmentCreateResponse;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.model.Shipment;
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

    ShipmentDto map(final Shipment shipment);

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

    default ShipmentControlCenterResponseApi map(final ShipmentRouteLog controlCenter) {
        return new ShipmentControlCenterResponseApi(map(controlCenter.shipment()), controlCenter.routeLog());
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
		return new MoneyApi(amount.getAmount(), amount.getCurrency().name());
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
