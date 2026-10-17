package com.warehouse.platformadministration.infrastructure.adapter.secondary;

import com.warehouse.platformadministration.infrastructure.adapter.secondary.entity.OperatorDataErasureEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorDataErasureReadRepository extends JpaRepository<OperatorDataErasureEntity, Long> {

    Optional<OperatorDataErasureEntity> findFirstByOperatorIdOrderByExecutedAtDesc(Long operatorId);
}
