package com.warehouse.deliveryreject.infrastructure.adapter.secondary;

import com.warehouse.deliveryreject.infrastructure.adapter.secondary.entity.RejectReasonEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RejectReasonReadRepository extends JpaRepository<RejectReasonEntity, Long> {
}
