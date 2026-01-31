package com.warehouse.process.infrastructure.adapter.secondary.entity.write;

import java.util.HashSet;
import java.util.Set;

import com.warehouse.process.infrastructure.adapter.secondary.entity.ProcessLogBaseEntity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "process_logs")
@Access(AccessType.FIELD)
@SuperBuilder
public class ProcessLogWriteEntity extends ProcessLogBaseEntity {

    @OneToMany(
            mappedBy = "processLog",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private Set<CommunicationLogWriteEntity> communicationLogs;

    protected ProcessLogWriteEntity() {

    }

    public Set<CommunicationLogWriteEntity> getCommunicationLogs() {
        if (communicationLogs == null) {
            this.communicationLogs = new HashSet<>();
        }
        return this.communicationLogs;
    }

    public void addCommunicationLog(final CommunicationLogWriteEntity child) {
        this.communicationLogs.add(child);
    }
}
