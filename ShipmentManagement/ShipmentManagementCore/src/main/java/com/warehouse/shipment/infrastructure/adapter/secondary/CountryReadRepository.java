package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.CountryEntity;

public interface CountryReadRepository extends JpaRepository<CountryEntity, CountryCode> {
}
