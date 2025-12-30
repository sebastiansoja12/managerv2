package com.warehouse.returntoken.domain.service;


import java.time.Instant;
import java.util.UUID;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.returntoken.domain.exception.ReturnTokenNotFoundException;
import com.warehouse.returntoken.domain.generator.ReturnTokenGenerator;
import com.warehouse.returntoken.domain.model.ReturnPackageRequest;
import com.warehouse.returntoken.domain.port.secondary.ReturnTokenRepository;
import com.warehouse.returntoken.domain.vo.ReturnPackageResponse;
import com.warehouse.returntoken.domain.vo.ReturnToken;
import com.warehouse.returntoken.infrastructure.adapter.secondary.entity.ReturnTokenId;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ReturnTokenServiceImpl implements ReturnTokenService {

    private final ReturnTokenRepository returnTokenRepository;

    @Override
    public ReturnPackageResponse sign(final ReturnPackageRequest returnPackageRequest) {
        final ShipmentId shipmentId = returnPackageRequest.getShipmentId();
        final ReturnTokenId returnTokenId = nextReturnTokenId();
        final String token = ReturnTokenGenerator.generateReturnToken(shipmentId);
		final ReturnToken returnToken = ReturnToken.builder()
                .returnTokenId(returnTokenId)
                .token(token)
				.expiresAt(Instant.now().plusSeconds(720000))
                .createdAt(Instant.now())
				.shipmentId(returnPackageRequest.getShipmentId())
                .updatedAt(Instant.now())
                .build();
        returnTokenRepository.create(returnToken);
        return new ReturnPackageResponse(shipmentId, returnToken, null);
    }

    private ReturnTokenId nextReturnTokenId() {
        return new ReturnTokenId(Math.abs(UUID.randomUUID().getLeastSignificantBits()));
    }

    @Override
    public ReturnToken findByShipmentId(final ShipmentId shipmentId) {
        return returnTokenRepository
                .findByShipmentId(shipmentId)
                .map(ReturnToken::from)
                .orElseThrow(() -> new ReturnTokenNotFoundException("Return token not found"));
    }

    @Override
    public Boolean exists(final ReturnToken returnToken) {
        return returnTokenRepository.exists(returnToken.getToken());
    }
}
