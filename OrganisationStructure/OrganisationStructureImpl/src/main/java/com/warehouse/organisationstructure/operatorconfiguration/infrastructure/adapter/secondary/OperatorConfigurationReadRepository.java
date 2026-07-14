package com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.organisationstructure.operatorconfiguration.infrastructure.adapter.secondary.entity.OperatorConfigurationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("organisationstructure.operatorConfigurationReadRepository")
public interface OperatorConfigurationReadRepository extends JpaRepository<OperatorConfigurationEntity, OperatorId> {
}
