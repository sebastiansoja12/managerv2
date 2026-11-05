package com.warehouse.returning.infrastructure.adapter.secondary;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnReadModelEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.DepartmentId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.OperatorId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReturnReadModelEntityRepository extends JpaRepository<ReturnReadModelEntity, ReturnId> {

    @Query("SELECT r FROM ReturnReadModelEntity r "
            + "WHERE r.assignedDepartmentId = :departmentId AND r.operatorId = :operatorId")
    Page<ReturnReadModelEntity> findByDepartmentIdAndOperatorId(
            @Param("departmentId") final DepartmentId departmentId,
            @Param("operatorId") final OperatorId operatorId,
            final Pageable pageable);
}
