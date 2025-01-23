package com.warehouse.returntoken.domain.service;


import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.generator.ReturnTokenGenerator;
import com.warehouse.returntoken.domain.model.ReturnPackageRequest;
import com.warehouse.returntoken.domain.port.secondary.ReturnTokenRepository;
import com.warehouse.returntoken.domain.vo.ReturnPackageResponse;
import com.warehouse.returntoken.domain.vo.ReturnToken;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnTokenServiceImpl implements ReturnTokenService {

    private final ReturnTokenRepository returnTokenRepository;

    @Override
    public ReturnPackageResponse sign(final ReturnPackageRequest returnPackageRequest) {
        final ShipmentId shipmentId = returnPackageRequest.getShipmentId();
        final ReturnToken returnToken = ReturnTokenGenerator.generateReturnToken(shipmentId);
        return new ReturnPackageResponse(shipmentId, returnToken, null);
    }


    @Override
    public ReturnToken findByShipmentId(final ShipmentId shipmentId) {
        return returnTokenRepository
                .findByShipmentId(shipmentId)
                .map(ReturnToken::from)
                .orElseGet(ReturnToken::empty);
    }

    @Override
    public Boolean exists(final ReturnToken returnToken) {
        return returnTokenRepository.exists(returnToken.getValue());
    }
}
