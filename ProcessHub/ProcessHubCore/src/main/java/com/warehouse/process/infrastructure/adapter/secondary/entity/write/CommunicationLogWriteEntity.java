package com.warehouse.process.infrastructure.adapter.secondary.entity.write;


import org.hibernate.annotations.Immutable;

import com.warehouse.process.infrastructure.adapter.secondary.entity.CommunicationLogBaseEntity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "communication_log_details")
@Access(AccessType.FIELD)
@Immutable
@Builder
public class CommunicationLogWriteEntity extends CommunicationLogBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_log_id", columnDefinition = "BINARY(16)")
    private ProcessLogWriteEntity processLog;

    protected CommunicationLogWriteEntity() {

    }

    public ProcessLogWriteEntity getProcessLog() {
        return processLog;
    }
}
