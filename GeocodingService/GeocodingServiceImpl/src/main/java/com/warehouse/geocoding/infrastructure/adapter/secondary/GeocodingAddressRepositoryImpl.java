package com.warehouse.geocoding.infrastructure.adapter.secondary;

import java.util.Optional;

import com.warehouse.commonassets.enumeration.GeocodingProvider;
import com.warehouse.geocoding.application.port.secondary.GeocodingAddressRepository;
import com.warehouse.geocoding.domain.vo.Coordinates;
import com.warehouse.geocoding.domain.vo.NormalizedGeocodingAddress;

public class GeocodingAddressRepositoryImpl implements GeocodingAddressRepository {

    private final GeocodingAddressReadRepository repository;

    public GeocodingAddressRepositoryImpl(final GeocodingAddressReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<Coordinates> findCoordinates(final NormalizedGeocodingAddress address) {
        return repository.findByCountryCodeAndPostalCodeAndCityAndStreetAndHouseNumber(address.countryCode(),
                        address.postalCode(), address.city(), address.street(), address.houseNumber())
                .map(entity -> new Coordinates(entity.getLatitude(), entity.getLongitude()));
    }

    @Override
    public void saveIfAbsent(final NormalizedGeocodingAddress address, final Coordinates coordinates,
                             final GeocodingProvider provider) {
        repository.insertIfAbsent(address.countryCode(), address.postalCode(), address.city(), address.street(),
                address.houseNumber(), coordinates.lat(), coordinates.lon(),
                provider == null ? null : provider.name());
    }
}
