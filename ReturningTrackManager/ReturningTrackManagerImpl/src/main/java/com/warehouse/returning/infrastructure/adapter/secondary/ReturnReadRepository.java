package com.warehouse.returning.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.warehouse.returning.infrastructure.adapter.secondary.entity.ReturnPackageEntity;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ReturnId;
import com.warehouse.returning.infrastructure.adapter.secondary.entity.identificator.ShipmentId;

@Repository
public interface ReturnReadRepository extends JpaRepository<ReturnPackageEntity, ReturnId> {

    @Query("SELECT r FROM ReturnPackageEntity r WHERE r.shipmentId = :shipmentId AND r.returnStatus <> 'CANCELLED'")
    Optional<ReturnPackageEntity> findByShipmentId(@Param("shipmentId") final ShipmentId shipmentId);

    @Query("SELECT r FROM ReturnPackageEntity r WHERE r.returnId = :returnId AND r.returnStatus <> 'CANCELLED'")
    Optional<ReturnPackageEntity> findById(@Param("returnId") final ReturnId returnId);

    @Query("SELECT r FROM ReturnPackageEntity r "
            + "WHERE r.assignedDepartmentCode.value = :departmentCode AND r.operatorId = :operatorId")
    Page<ReturnPackageEntity> findByDepartmentCodeAndOperatorId(
            @Param("departmentCode") final String departmentCode,
            @Param("operatorId") final Long operatorId,
            final Pageable pageable);

}
