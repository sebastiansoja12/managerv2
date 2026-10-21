package com.warehouse.driverhub.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.driverhub.infrastructure.adapter.secondary.entity.CarrierDriverEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CarrierDriverReadRepository extends JpaRepository<CarrierDriverEntity, DriverId> {
}
