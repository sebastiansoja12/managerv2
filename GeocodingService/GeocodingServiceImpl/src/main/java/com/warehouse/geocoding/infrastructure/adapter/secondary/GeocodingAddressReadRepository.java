package com.warehouse.geocoding.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.GeocodingAddressEntity;

public interface GeocodingAddressReadRepository extends JpaRepository<GeocodingAddressEntity, Long> {

    Optional<GeocodingAddressEntity> findByCountryCodeAndPostalCodeAndCityAndStreetAndHouseNumber(
            final String countryCode, final String postalCode, final String city, final String street,
            final String houseNumber);

    @Modifying
    @Transactional
    @Query(value = """
            INSERT INTO geocoding_addresses (country_code, postal_code, city, street, house_number,
                                             latitude, longitude, provider, geocoded_at, created_at, updated_at)
            VALUES (:countryCode, :postalCode, :city, :street, :houseNumber,
                    :latitude, :longitude, :provider, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
            ON CONFLICT DO NOTHING
            """, nativeQuery = true)
    int insertIfAbsent(@Param("countryCode") final String countryCode,
                       @Param("postalCode") final String postalCode,
                       @Param("city") final String city,
                       @Param("street") final String street,
                       @Param("houseNumber") final String houseNumber,
                       @Param("latitude") final Double latitude,
                       @Param("longitude") final Double longitude,
                       @Param("provider") final String provider);
}
