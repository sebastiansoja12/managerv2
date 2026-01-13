package com.warehouse.process.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.vo.CommunicationLogId;

import lombok.Builder;

@Builder
public class CommunicationLogDetail {
    private CommunicationLogId communicationLogId;
    private DeviceId deviceId;
    private ProcessType processType;
    private ServiceType serviceType;
    private UserId createdBy;
    private UserId updatedBy;
    private DepartmentCode departmentCode;
    private String sourceService;
    private String targetService;
    private String request;
    private String response;
    private Instant createdAt;
    private Instant updatedAt;
    private String faultDescription;

    public void changeSourceService(final String sourceService) {
        this.sourceService = sourceService;
        markAsModified();
    }

    public void changeTargetService(final String targetService) {
        this.targetService = targetService;
        markAsModified();
    }

    public void changeFaultDescription(final String faultDescription) {
        this.faultDescription = faultDescription;
        markAsModified();
    }

    public void changeResponse(final String response) {
        this.response = response;
        markAsModified();
    }

    public void changeRequest(final String request) {
        this.request = request;
        markAsModified();
    }

    private void markAsModified() {
        this.updatedAt = Instant.now();
    }

    public Long getId() {
        return communicationLogId.value();
    }

    public ProcessType getProcessType() {
        return processType;
    }

    public CommunicationLogId getCommunicationLogId() {
        return communicationLogId;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public String getRequest() {
        return request;
    }

    public String getResponse() {
        return response;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public String getSourceService() {
        return sourceService;
    }

    public String getTargetService() {
        return targetService;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public UserId getCreatedBy() {
        return createdBy;
    }

    public DepartmentCode getDepartmentCode() {
        return departmentCode;
    }

    public DeviceId getDeviceId() {
        return deviceId;
    }

    public UserId getUpdatedBy() {
        return updatedBy;
    }
}
