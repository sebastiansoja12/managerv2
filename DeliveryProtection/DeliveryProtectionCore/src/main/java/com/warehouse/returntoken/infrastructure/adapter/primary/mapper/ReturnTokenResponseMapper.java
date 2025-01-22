package com.warehouse.returntoken.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.vo.ReturnTokenResponse;
import com.warehouse.returntoken.domain.vo.CrossCourierDelivery;
import com.warehouse.returntoken.domain.vo.ReturnPackageResponse;
import com.warehouse.returntoken.domain.vo.ReturnToken;
import com.warehouse.returntoken.domain.vo.Supplier;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.*;

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

    SupplierDto map(final Supplier supplier);

    default ReturnPackageResponseDto map(final ReturnPackageResponse returnPackageResponse) {
        final ShipmentIdDto shipmentId = map(returnPackageResponse.getShipmentId());
        final ReturnTokenDto returnToken = map(returnPackageResponse.getReturnToken());
        final CrossCourierDeliveryDto crossCourierDelivery = map(returnPackageResponse.getCrossCourierDelivery());
        return new ReturnPackageResponseDto(returnToken, shipmentId, crossCourierDelivery);
    }

    CrossCourierDeliveryDto map(final CrossCourierDelivery crossCourierDelivery);

    ReturnTokenDto map(final ReturnToken returnToken);

    default ShipmentIdDto map(final ShipmentId shipmentId) {
        return new ShipmentIdDto(shipmentId.value());
    }
}
