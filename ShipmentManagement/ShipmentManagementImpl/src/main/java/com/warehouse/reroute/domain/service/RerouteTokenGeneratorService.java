package com.warehouse.reroute.domain.service;

public interface RerouteTokenGeneratorService {
    int generate(Long parcelId, String email);
}
