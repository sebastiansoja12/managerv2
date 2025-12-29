package com.warehouse.process.infrastructure.adapter.secondary.entity.write;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Immutable;

import com.warehouse.process.infrastructure.adapter.secondary.entity.ProcessLogBaseEntity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "process_logs")
@Access(AccessType.FIELD)
@Immutable
@SuperBuilder
public class ProcessLogWriteEntity extends ProcessLogBaseEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "processLog")
    private Set<CommunicationLogWriteEntity> communicationLogs = new HashSet<>();

    protected ProcessLogWriteEntity() {

    }

    public Set<CommunicationLogWriteEntity> getCommunicationLogs() {
        return Collections.unmodifiableSet(communicationLogs);
    }

    public void addCommunicationLog(final CommunicationLogWriteEntity child) {
        this.communicationLogs.add(child);
    }
}
