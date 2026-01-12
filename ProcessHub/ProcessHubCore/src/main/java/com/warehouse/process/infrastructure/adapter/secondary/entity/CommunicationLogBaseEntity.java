package com.warehouse.process.infrastructure.adapter.secondary.entity;

import java.time.Instant;

import org.hibernate.annotations.Immutable;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.vo.CommunicationLogId;

import jakarta.persistence.*;
import lombok.Builder;

@MappedSuperclass
@Access(AccessType.FIELD)
@Immutable
@Builder
public abstract class CommunicationLogBaseEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "communication_log_id", nullable = false))
    private CommunicationLogId communicationLogId;

    @Column(name = "device_id")
    @AttributeOverride(name = "value", column = @Column(name = "device_id"))
    private DeviceId deviceId;

    @Enumerated(EnumType.STRING)
    @Column(name = "process_type")
    private ProcessType processType;

    @Enumerated(EnumType.STRING)
    @Column(name = "service_type")
    private ServiceType serviceType;

    @Column(name = "created_by")
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId createdBy;

    @Column(name = "updated_by")
    @AttributeOverride(name = "value", column = @Column(name = "user_id"))
    private UserId updatedBy;

    @Column(name = "department_code")
    @AttributeOverride(name = "value", column = @Column(name = "department_code"))
    private DepartmentCode departmentCode;

    @Column(name = "source_service")
    private String sourceService;

    @Column(name = "target_service")
    private String targetService;

    @Lob
    @Column(name = "request")
    private String request;

    @Lob
    @Column(name = "response")
    private String response;

    @Lob
    @Column(name = "fault_description")
    private String faultDescription;

    @Column(name = "created_at")
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    protected CommunicationLogBaseEntity() {
    }

    public CommunicationLogId getCommunicationLogId() { return communicationLogId; }
    public DeviceId getDeviceId() { return deviceId; }
    public ProcessType getProcessType() { return processType; }
    public ServiceType getServiceType() { return serviceType; }
    public UserId getCreatedBy() { return createdBy; }
    public UserId getUpdatedBy() { return updatedBy; }
    public DepartmentCode getDepartmentCode() { return departmentCode; }
    public String getSourceService() { return sourceService; }
    public String getTargetService() { return targetService; }
    public String getRequest() { return request; }
    public String getResponse() { return response; }
    public String getFaultDescription() { return faultDescription; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CommunicationLogBaseEntity)) return false;
        CommunicationLogBaseEntity that = (CommunicationLogBaseEntity) o;
        return communicationLogId != null && communicationLogId.equals(that.communicationLogId);
    }

    @Override
    public int hashCode() {
        return 31;
    }
}

