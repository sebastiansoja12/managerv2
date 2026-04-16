package com.warehouse.reroute.domain.service;

import com.warehouse.reroute.infrastructure.adapter.secondary.entity.RerouteTokenEntity;
import com.warehouse.reroute.infrastructure.adapter.secondary.RerouteTokenReadRepository;
import lombok.AllArgsConstructor;

import java.time.Instant;
import java.util.Optional;

@AllArgsConstructor
public class RerouteTokenValidatorServiceImpl implements RerouteTokenValidatorService {

    private final RerouteTokenReadRepository repository;

    @Override
    public boolean validate(Integer token) {
        final Optional<RerouteTokenEntity> rerouteToken = repository.findByToken(token);
        return rerouteToken.isPresent() && rerouteToken.get().getExpiryDate().isAfter(Instant.now());
    }
}
