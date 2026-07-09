package com.warehouse.process.infrastructure.adapter.secondary.entity.read;

import java.util.HashSet;
import java.util.Set;

import com.warehouse.process.infrastructure.adapter.secondary.entity.ProcessLogBaseEntity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "process_logs_rd")
@Access(AccessType.FIELD)
@SuperBuilder
public class ProcessLogReadEntity extends ProcessLogBaseEntity {

    @OneToMany(
            mappedBy = "processLog",
            fetch = FetchType.LAZY,
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private Set<CommunicationLogReadEntity> communicationLogs = new HashSet<>();

    protected ProcessLogReadEntity() {

    }

    public Set<CommunicationLogReadEntity> getCommunicationLogs() {
        if (communicationLogs == null) {
            communicationLogs = new HashSet<>();
        }
        return communicationLogs;
    }

    public void addCommunicationLog(final CommunicationLogReadEntity child) {
        getCommunicationLogs().add(child);
    }

}
