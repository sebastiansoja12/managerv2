package com.warehouse.process.infrastructure.adapter.secondary;

import com.warehouse.process.infrastructure.adapter.secondary.entity.identificator.ProcessLogId;
import org.springframework.data.jpa.repository.JpaRepository;

import com.warehouse.process.infrastructure.adapter.secondary.entity.read.ProcessLogReadEntity;
import org.springframework.stereotype.Repository;

@Repository
public interface ProcessLogReadRepository extends JpaRepository<ProcessLogReadEntity, ProcessLogId> {
}
