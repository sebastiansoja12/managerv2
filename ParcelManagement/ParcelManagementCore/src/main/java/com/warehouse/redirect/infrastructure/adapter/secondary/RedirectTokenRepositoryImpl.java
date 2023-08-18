package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.redirect.domain.vo.RedirectToken;
import com.warehouse.redirect.domain.vo.Token;
import com.warehouse.redirect.domain.port.secondary.RedirectTokenRepository;
import com.warehouse.redirect.infrastructure.adapter.secondary.entity.RedirectTokenEntity;
import com.warehouse.redirect.infrastructure.adapter.secondary.mapper.RedirectTokenMapper;
import lombok.AllArgsConstructor;

import java.time.Instant;

@AllArgsConstructor
public class RedirectTokenRepositoryImpl implements RedirectTokenRepository {

    private final RedirectTokenReadRepository redirectTokenReadRepository;

    private final RedirectTokenMapper mapper;

    private final static Long expiration = 86400L;

    @Override
    public Token save(RedirectToken redirectToken) {
        final RedirectTokenEntity entity = mapper.map(redirectToken);
        entity.setCreatedDate(Instant.now());
        entity.setExpiryDate(Instant.now().plusSeconds(expiration));
        redirectTokenReadRepository.save(entity);
        return new Token(entity.getToken());
    }
}
