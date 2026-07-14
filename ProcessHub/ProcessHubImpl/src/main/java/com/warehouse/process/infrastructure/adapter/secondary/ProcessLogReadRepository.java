package com.warehouse.process.infrastructure.adapter.secondary;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;

@Repository
public interface ProcessLogReadRepository extends JpaRepository<ProcessLogReadEntity, ProcessId> {
    @EntityGraph(attributePaths = "communicationLogs")
    @Query("""
            select distinct p
            from ProcessLogReadEntity p
            left join p.communicationLogs c
            where p.operatorId = :operatorId
               or c.operatorId = :operatorId
            """)
    Page<ProcessLogReadEntity> findAllByOperatorId(final OperatorId operatorId,
                                                   final Pageable pageable);

    @Query("""
            select distinct p
            from ProcessLogReadEntity p
            left join fetch p.communicationLogs c
            where p.processId = :processId
              and (p.operatorId = :operatorId
                   or c.operatorId = :operatorId)
            """)
    Optional<ProcessLogReadEntity> findByIdAndOperatorId(final ProcessId processId,
                                                         final OperatorId operatorId);

    Optional<ProcessLogReadEntity> findByProcessId(final ProcessId processId);
}
