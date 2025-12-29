package com.warehouse.process.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;

@Repository
public interface ProcessLogWriteRepository extends JpaRepository<ProcessLogWriteEntity, ProcessId> {
}
