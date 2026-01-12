package com.warehouse.process.infrastructure.adapter.secondary.entity.write;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Immutable;

import com.warehouse.process.infrastructure.adapter.secondary.entity.ProcessLogBaseEntity;

import jakarta.persistence.*;
import lombok.Builder;

@Entity
@Table(name = "process_logs")
@Access(AccessType.FIELD)
@Immutable
@Builder
public class ProcessLogWriteEntity extends ProcessLogBaseEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "processLog")
    private Set<CommunicationLogWriteEntity> communicationLogs = new HashSet<>();

    protected ProcessLogWriteEntity() {

    }

    public Set<CommunicationLogWriteEntity> getCommunicationLogs() {
        return Collections.unmodifiableSet(communicationLogs);
    }
}
