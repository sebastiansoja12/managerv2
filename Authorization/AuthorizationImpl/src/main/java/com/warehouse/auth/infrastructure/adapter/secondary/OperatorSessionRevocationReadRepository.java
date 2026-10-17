package com.warehouse.auth.infrastructure.adapter.secondary;

import com.warehouse.auth.infrastructure.adapter.secondary.entity.OperatorSessionRevocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OperatorSessionRevocationReadRepository extends JpaRepository<OperatorSessionRevocationEntity, Long> {
}
