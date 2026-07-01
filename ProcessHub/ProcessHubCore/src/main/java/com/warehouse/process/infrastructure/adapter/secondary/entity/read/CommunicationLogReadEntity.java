package com.warehouse.process.infrastructure.adapter.secondary.entity.read;

import com.warehouse.process.infrastructure.adapter.secondary.entity.CommunicationLogBaseEntity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;


@Entity
@Table(name = "communication_log_details_rd")
@Access(AccessType.FIELD)
@SuperBuilder
public class CommunicationLogReadEntity extends CommunicationLogBaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "process_id", columnDefinition = "BINARY(16)")
    private ProcessLogReadEntity processLog;

    protected CommunicationLogReadEntity() {
    }

    public ProcessLogReadEntity getProcessLog() {
        return processLog;
    }
}
