package com.warehouse.process.infrastructure.adapter.secondary;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.warehouse.process.domain.vo.CommunicationLogId;
import com.warehouse.process.infrastructure.adapter.secondary.entity.read.CommunicationLogReadEntity;

@Repository
public interface CommunicationLogReadRepository extends JpaRepository<CommunicationLogReadEntity, CommunicationLogId> {
}
