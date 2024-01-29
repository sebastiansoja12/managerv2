package com.warehouse.create.infrastructure.adapter.secondary;

import com.warehouse.create.infrastructure.adapter.secondary.entity.DeliveryCreateEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryCreateReadRepository extends JpaRepository<DeliveryCreateEntity, String> {
}
