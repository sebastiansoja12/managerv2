package com.warehouse.logisticsdelivery.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.SignatureId;
import com.warehouse.logisticsdelivery.infrastructure.adapter.secondary.entity.SignatureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SignatureReadRepository extends JpaRepository<SignatureEntity, SignatureId> {
}
