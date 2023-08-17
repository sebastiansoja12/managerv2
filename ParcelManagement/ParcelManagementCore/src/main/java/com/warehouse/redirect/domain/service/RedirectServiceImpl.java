package com.warehouse.redirect.domain.service;

import com.warehouse.redirect.domain.model.RedirectToken;
import com.warehouse.redirect.domain.model.Token;
import com.warehouse.redirect.domain.port.secondary.RedirectTokenRepository;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RedirectServiceImpl implements RedirectService {

    private final RedirectTokenRepository redirectTokenRepository;

    @Override
    public Token saveRedirectToken(RedirectToken redirectToken) {
        return redirectTokenRepository.save(redirectToken);
    }
}
