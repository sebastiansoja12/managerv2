package com.warehouse.geocoding.infrastructure.adapter.secondary;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.PostalCodeEntity;

public interface PostalCodeReadRepository
        extends JpaRepository<PostalCodeEntity, PostalCodeEntity.PostalCodeEntityId> {

    List<PostalCodeEntity> findByPostalCode(final String postalCode);

    List<PostalCodeEntity> findTop20ByPostalCodeStartingWithOrderByPostalCodeAscCityAsc(final String postalCodePrefix);

    List<PostalCodeEntity> findTop20ByCityStartingWithIgnoreCaseOrderByCityAscPostalCodeAsc(final String cityPrefix);
}
