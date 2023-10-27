package com.warehouse.parcelstatuschange.infrastructure.adapter.secondary;

import com.warehouse.parcelstatuschange.infrastructure.adapter.secondary.entity.ParcelEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ParcelStatusReadRepository extends JpaRepository<ParcelEntity, Long> {
}
