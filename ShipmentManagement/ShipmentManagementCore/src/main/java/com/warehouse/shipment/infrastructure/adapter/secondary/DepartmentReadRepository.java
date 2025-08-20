package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.enumeration.CountryCode;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.DepartmentEntity;

@Repository(value = "shipment.departmentReadRepository")
public interface DepartmentReadRepository extends JpaRepository<DepartmentEntity, DepartmentCode> {
    boolean existsAnyByCountryCode(final CountryCode countryCode);
}
