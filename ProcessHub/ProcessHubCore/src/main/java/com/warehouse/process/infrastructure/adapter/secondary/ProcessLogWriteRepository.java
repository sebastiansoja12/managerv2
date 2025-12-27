package com.warehouse.process.infrastructure.adapter.secondary;

import com.warehouse.process.infrastructure.adapter.secondary.entity.identificator.ProcessLogId;
import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.process.infrastructure.adapter.secondary.entity.write.ProcessLogWriteEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessLogWriteRepository extends JpaRepository<ProcessLogWriteEntity, ProcessLogId> {
}
