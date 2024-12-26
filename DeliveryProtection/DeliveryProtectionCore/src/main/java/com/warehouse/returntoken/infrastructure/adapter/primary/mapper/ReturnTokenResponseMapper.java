package com.warehouse.returntoken.infrastructure.adapter.primary.mapper;

import java.util.List;

import org.mapstruct.Mapper;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.model.ReturnPackageResponse;
import com.warehouse.returntoken.domain.model.ReturnToken;
import com.warehouse.returntoken.domain.model.ReturnTokenResponse;
import com.warehouse.returntoken.domain.vo.Supplier;
import com.warehouse.returntoken.infrastructure.adapter.primary.dto.*;

@Mapper
public interface ReturnTokenResponseMapper {

    default ReturnTokenResponseDto map(final ReturnTokenResponse returnTokenResponse) {
        final List<ReturnPackageResponseDto> deliveryReturnSignatures = returnTokenResponse.getReturnPackageResponses()
                .stream()
                .map(this::map)
                .toList();
        final SupplierDto supplierDto = map(returnTokenResponse.getSupplier());
        return new ReturnTokenResponseDto(deliveryReturnSignatures, supplierDto);
    }

    SupplierDto map(final Supplier supplier);

    default ReturnPackageResponseDto map(final ReturnPackageResponse returnPackageResponse) {
        final ShipmentIdDto shipmentId = map(returnPackageResponse.getShipmentId());
        final ReturnTokenDto returnToken = map(returnPackageResponse.getReturnToken());
        return new ReturnPackageResponseDto(returnToken, shipmentId);
    }

    ReturnTokenDto map(final ReturnToken returnToken);

    ShipmentIdDto map(final ShipmentId shipmentId);
}
