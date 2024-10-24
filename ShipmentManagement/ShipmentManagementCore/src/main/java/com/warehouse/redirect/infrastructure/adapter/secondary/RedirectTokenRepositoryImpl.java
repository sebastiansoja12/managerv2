package com.warehouse.redirect.infrastructure.adapter.secondary;

import java.time.LocalDateTime;

import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.redirect.domain.port.secondary.RedirectTokenRepository;
import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.domain.vo.Token;
import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;
import com.warehouse.redirect.infrastructure.adapter.secondary.exception.RedirectTokenNotFoundException;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.RedirectTokenMapper;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectTokenRepositoryImpl implements RedirectTokenRepository {

    private final RedirectTokenReadRepository redirectTokenReadRepository;

    private final RedirectTokenMapper mapper;

    private final static Long expiration = 86400L;

    private final static String exceptionMessage = "Redirect token was not found for shipment: %s";

    @Override
    public Token save(RedirectToken redirectToken) {
        final RedirectTokenEntity entity = mapper.map(redirectToken);
        entity.setCreatedDate(LocalDateTime.now());
        entity.setExpiryDate(LocalDateTime.now().plusSeconds(expiration));
        redirectTokenReadRepository.save(entity);
        return new Token(entity.getToken());
    }

	@Override
	public RedirectToken findByShipmentId(final ShipmentId shipmentId) {
		return redirectTokenReadRepository.findByParcelId(shipmentId.getValue())
                .map(RedirectToken::from)
                .orElseThrow(() -> new RedirectTokenNotFoundException(String.format(exceptionMessage, shipmentId.getValue())));
	}

    @Override
    public void update(RedirectToken redirectToken) {
        final RedirectTokenEntity entity = mapper.map(redirectToken);
        this.redirectTokenReadRepository.save(entity);
    }
}
