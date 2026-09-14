package com.warehouse.geocoding.infrastructure.adapter.secondary;

import java.util.List;

import com.warehouse.geocoding.application.port.secondary.PostalCodeRepository;
import com.warehouse.geocoding.domain.vo.PostalCode;
import com.warehouse.geocoding.infrastructure.adapter.secondary.entity.PostalCodeEntity;

public class PostalCodeRepositoryImpl implements PostalCodeRepository {

    private final PostalCodeReadRepository repository;

    public PostalCodeRepositoryImpl(final PostalCodeReadRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<PostalCode> findByPostalCode(final String postalCode) {
        return map(repository.findByPostalCode(postalCode));
    }

    @Override
    public List<PostalCode> findByPostalCodePrefix(final String postalCodePrefix) {
        return map(repository.findTop20ByPostalCodeStartingWithOrderByPostalCodeAscCityAsc(postalCodePrefix));
    }

    @Override
    public List<PostalCode> findByCityPrefix(final String cityPrefix) {
        return map(repository.findTop20ByCityStartingWithIgnoreCaseOrderByCityAscPostalCodeAsc(cityPrefix));
    }

    private List<PostalCode> map(final List<PostalCodeEntity> entities) {
        return entities.stream()
                .map(entity -> new PostalCode(entity.getCountryCode(), entity.getPostalCode(), entity.getCity(),
                        entity.getRegionCode()))
                .toList();
    }
}
