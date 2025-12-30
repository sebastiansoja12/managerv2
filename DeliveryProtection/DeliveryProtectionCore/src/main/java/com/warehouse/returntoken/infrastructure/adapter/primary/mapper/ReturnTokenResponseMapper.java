package com.warehouse.returntoken.infrastructure.adapter.primary.mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.vo.*;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.*;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper
public interface ReturnTokenResponseMapper {

    default ReturnTokenResponseDto map(final ReturnTokenResponse returnTokenResponse) {
        final List<ReturnPackageResponseDto> returnPackageResponses = returnTokenResponse.getReturnPackageResponses()
                .stream()
                .map(this::map)
                .toList();
        final SupplierDto supplierDto = map(returnTokenResponse.getSupplier());
        return new ReturnTokenResponseDto(returnPackageResponses, supplierDto);
    }

    default SupplierDto map(final Supplier supplier) {
        return new SupplierDto(new SupplierCodeDto(supplier.getSupplierCode().value()));
    }

    default ReturnPackageResponseDto map(final ReturnPackageResponse returnPackageResponse) {
        final ShipmentIdDto shipmentId = map(returnPackageResponse.getShipmentId());
        final ReturnTokenDto returnToken = map(returnPackageResponse.getReturnToken());
        final CrossCourierDeliveryDto crossCourierDelivery = map(returnPackageResponse.getCrossCourierDelivery());
        return new ReturnPackageResponseDto(returnToken, shipmentId, crossCourierDelivery);
    }

    CrossCourierDeliveryDto map(final CrossCourierDelivery crossCourierDelivery);

    String map(Object value);

    default ReturnTokenDto map(final ReturnToken returnToken) {
        return new ReturnTokenDto(returnToken.getToken(),
                new ShipmentIdDto(returnToken.getShipmentId().getValue()),
                returnToken.getExpiresAt());
    }

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.getValue());
    }
}
