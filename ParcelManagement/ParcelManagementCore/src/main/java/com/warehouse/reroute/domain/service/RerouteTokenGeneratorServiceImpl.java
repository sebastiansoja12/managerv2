package com.warehouse.reroute.domain.service;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class RerouteTokenGeneratorServiceImpl implements RerouteTokenGeneratorService {

    @Override
    public int generate(Long parcelId, String email) {

        final String combinedString = parcelId.toString() + email;

        int hashCode = getHashCode(combinedString);

        hashCode = Math.abs(hashCode);

        final int randomNumber = calculateRandomNumber(hashCode, parcelId);

        final String paddedRandomNumber = String.format("%06d", randomNumber);

        return Integer.parseInt(paddedRandomNumber);
    }

    private int calculateRandomNumber(int hashCode, Long parcelId) {
        return (int) (hashCode % parcelId) * 100;
    }

    private int getHashCode(String combinedString) {
        return combinedString.hashCode();
    }
}
