package com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operator.infrastructure.adapter.secondary.entity.OperatorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository("organisationstructure.operatorReadRepository")
public interface OperatorReadRepository extends JpaRepository<OperatorEntity, OperatorId> {
    @Query("select max(o.operatorId.value) from organisationstructure.OperatorEntity o")
    Optional<Long> findMaxOperatorIdValue();
}
