package com.warehouse.redirect.infrastructure.adapter.secondary;

import com.warehouse.redirect.domain.model.RedirectToken;
import com.warehouse.redirect.domain.model.Token;
import com.warehouse.redirect.domain.port.secondary.RedirectTokenRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectTokenRepositoryImpl implements RedirectTokenRepository {

    private final RedirectTokenReadRepository redirectTokenReadRepository;

    @Override
    public Token save(RedirectToken redirectToken) {
        return null;
    }
}
