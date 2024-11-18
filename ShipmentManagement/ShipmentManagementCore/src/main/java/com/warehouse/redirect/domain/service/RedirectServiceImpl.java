package com.warehouse.redirect.domain.service;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.redirect.domain.port.secondary.RedirectTokenRepository;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.domain.vo.Token;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectServiceImpl implements RedirectService {

    private final RedirectTokenRepository redirectTokenRepository;

    @Override
    public Token saveRedirectToken(RedirectToken redirectToken) {
        return redirectTokenRepository.save(redirectToken);
    }

    @Override
    public void invalidateToken(final ShipmentId shipmentId) {
        final RedirectToken redirectToken = this.redirectTokenRepository.findByShipmentId(shipmentId);
        redirectToken.invalidate();
        this.redirectTokenRepository.update(redirectToken);
    }
}
