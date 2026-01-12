package com.warehouse.process.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import org.hibernate.annotations.Immutable;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;

import jakarta.persistence.*;
import lombok.Builder;

@MappedSuperclass
@Access(AccessType.FIELD)
@Immutable
@Builder
public abstract class ProcessLogBaseEntity {

    @EmbeddedId
    @AttributeOverride(
            name = "value",
            column = @Column(name = "process_id", nullable = false, columnDefinition = "BINARY(16)")
    )
    private ProcessId processId;

    @Lob
    @Column(name = "request")
    private String request;

    @Lob
    @Column(name = "response")
    private String response;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private ProcessStatus status;

    @Column(name = "fault_description")
    private String faultDescription;

    @Embedded
    private DeviceInformationEmbeddable deviceInformation;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant createdAt;

    @Column(name = "modified_at", nullable = false, columnDefinition = "TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP")
    private Instant modifiedAt;

    protected ProcessLogBaseEntity() {
    }

    public ProcessId getProcessId() {
        return processId;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public DeviceInformationEmbeddable getDeviceInformation() {
        return deviceInformation;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProcessLogBaseEntity)) return false;
        ProcessLogBaseEntity that = (ProcessLogBaseEntity) o;
        return processId != null && processId.equals(that.processId);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}

