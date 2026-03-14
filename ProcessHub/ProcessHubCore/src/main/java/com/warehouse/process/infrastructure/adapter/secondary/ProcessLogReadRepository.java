package com.warehouse.process.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;

@Repository
public interface ProcessLogReadRepository extends JpaRepository<ProcessLogReadEntity, ProcessId> {
    @EntityGraph(attributePaths = "communicationLogs")
    @Query("""
            select distinct p
            from ProcessLogReadEntity p
            left join p.communicationLogs c
            where p.deviceInformation.departmentCode = :departmentCode
               or c.departmentCode = :departmentCode
            """)
    Page<ProcessLogReadEntity> findAllByDepartmentCode(final DepartmentCode departmentCode,
                                                       final Pageable pageable);

    @Query("""
            select distinct p
            from ProcessLogReadEntity p
            left join fetch p.communicationLogs c
            where p.processId = :processId
              and (p.deviceInformation.departmentCode = :departmentCode
                   or c.departmentCode = :departmentCode)
            """)
    Optional<ProcessLogReadEntity> findByIdAndDepartmentCode(final ProcessId processId,
                                                             final DepartmentCode departmentCode);

    Optional<ProcessLogReadEntity> findByProcessId(final ProcessId processId);
}
