package com.warehouse.shipment.infrastructure.adapter.secondary;

import com.warehouse.commonassets.enumeration.Country;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.shipment.infrastructure.adapter.secondary.entity.DepartmentEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DepartmentReadRepository extends JpaRepository<DepartmentEntity, DepartmentCode> {
    boolean existsAnyByCountry(final Country country);
}
