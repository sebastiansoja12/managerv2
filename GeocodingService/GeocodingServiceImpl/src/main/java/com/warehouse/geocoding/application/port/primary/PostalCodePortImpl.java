package com.warehouse.geocoding.application.port.primary;

import java.util.List;
import java.util.Optional;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.geocoding.application.port.secondary.PostalCodeRepository;
import com.warehouse.geocoding.domain.vo.PostalCode;

public class PostalCodePortImpl implements PostalCodePort {

    private static final int MINIMUM_QUERY_LENGTH = 2;

    private final PostalCodeRepository postalCodeRepository;

    public PostalCodePortImpl(final PostalCodeRepository postalCodeRepository) {
        this.postalCodeRepository = postalCodeRepository;
    }

    @Override
    public List<PostalCode> search(final String query) {
        final String trimmedQuery = query == null ? "" : query.trim();
        if (trimmedQuery.length() < MINIMUM_QUERY_LENGTH) {
            return List.of();
        }
        if (Character.isDigit(trimmedQuery.charAt(0))) {
            return postalCodeRepository.findByPostalCodePrefix(withPolishSeparator(trimmedQuery));
        }
        return postalCodeRepository.findByCityPrefix(trimmedQuery);
    }

    @Override
    public Optional<CountryCode> findCountryCode(final String postalCode, final String city) {
        if (postalCode == null || postalCode.isBlank()) {
            return Optional.empty();
        }
        final String trimmedPostalCode = postalCode.trim();
        List<PostalCode> postalCodes = postalCodeRepository.findByPostalCode(trimmedPostalCode);
        if (postalCodes.isEmpty()) {
            postalCodes = postalCodeRepository.findByPostalCode(withPolishSeparator(trimmedPostalCode));
        }
        final List<CountryCode> countryCodes = distinctCountryCodes(postalCodes);
        if (countryCodes.size() == 1) {
            return Optional.of(countryCodes.getFirst());
        }
        if (city == null) {
            return Optional.empty();
        }
        final List<CountryCode> cityCountryCodes = distinctCountryCodes(postalCodes.stream()
                .filter(code -> code.city().equalsIgnoreCase(city.trim()))
                .toList());
        return cityCountryCodes.size() == 1 ? Optional.of(cityCountryCodes.getFirst()) : Optional.empty();
    }

    private List<CountryCode> distinctCountryCodes(final List<PostalCode> postalCodes) {
        return postalCodes.stream()
                .map(PostalCode::countryCode)
                .distinct()
                .toList();
    }

    private String withPolishSeparator(final String postalCode) {
        if (postalCode.matches("\\d{3,5}")) {
            return postalCode.substring(0, 2) + "-" + postalCode.substring(2);
        }
        return postalCode;
    }
}
