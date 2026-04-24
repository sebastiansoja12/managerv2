package com.warehouse.process.infrastructure.adapter.secondary.entity.read;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.Immutable;

import com.warehouse.process.infrastructure.adapter.secondary.entity.ProcessLogBaseEntity;

import jakarta.persistence.*;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "process_logs_rd")
@Access(AccessType.FIELD)
@Immutable
@SuperBuilder
public class ProcessLogReadEntity extends ProcessLogBaseEntity {

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "processLog")
    private Set<CommunicationLogReadEntity> communicationLogs = new HashSet<>();

    protected ProcessLogReadEntity() {

    }

    public Set<CommunicationLogReadEntity> getCommunicationLogs() {
        return Collections.unmodifiableSet(communicationLogs);
    }
}
