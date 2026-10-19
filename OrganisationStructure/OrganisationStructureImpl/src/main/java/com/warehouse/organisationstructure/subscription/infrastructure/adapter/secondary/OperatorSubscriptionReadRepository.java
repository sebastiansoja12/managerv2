package com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.OperatorSubscriptionId;
import com.warehouse.organisationstructure.subscription.infrastructure.adapter.secondary.entity.OperatorSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OperatorSubscriptionReadRepository
        extends JpaRepository<OperatorSubscriptionEntity, OperatorSubscriptionId> {

    Optional<OperatorSubscriptionEntity> findByOperatorId(final OperatorId operatorId);
}
