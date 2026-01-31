package com.warehouse.process.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.enumeration.ServiceType;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.process.domain.vo.CommunicationLogId;
import com.warehouse.process.domain.vo.ShipmentUpdated;

import lombok.Builder;

public class CommunicationLogDetail {

    private final CommunicationLogId communicationLogId;
    private final Instant createdAt;

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
    private Instant updatedAt;
    private String faultDescription;

    @Builder
    private CommunicationLogDetail(final CommunicationLogId communicationLogId,
                                   final DeviceId deviceId,
                                   final ProcessType processType,
                                   final ServiceType serviceType,
                                   final UserId createdBy,
                                   final UserId updatedBy,
                                   final DepartmentCode departmentCode,
                                   final String sourceService,
                                   final String targetService,
                                   final String request,
                                   final String response,
                                   final Instant createdAt,
                                   final Instant updatedAt,
                                   final String faultDescription) {
        final Instant now = Instant.now();
        this.communicationLogId = communicationLogId;
        this.deviceId = deviceId;
        this.processType = processType;
        this.serviceType = serviceType;
        this.createdBy = createdBy;
        this.updatedBy = updatedBy;
        this.departmentCode = departmentCode;
        this.sourceService = sourceService;
        this.targetService = targetService;
        this.request = request;
        this.response = response;
        this.createdAt = createdAt != null ? createdAt : now;
        this.updatedAt = updatedAt != null ? updatedAt : this.createdAt;
        this.faultDescription = faultDescription;
    }

    public static CommunicationLogDetail newLog(final CommunicationLogId id,
                                                final ProcessType processType,
                                                final ServiceType serviceType) {
        final Instant now = Instant.now();
        return CommunicationLogDetail.builder()
                .communicationLogId(id)
                .processType(processType)
                .serviceType(serviceType)
                .createdAt(now)
                .updatedAt(now)
                .build();
    }

    public void applyShipmentUpdate(final ShipmentUpdated shipmentUpdated) {
        this.deviceId = shipmentUpdated.deviceId();
        this.sourceService = shipmentUpdated.sourceService();
        this.targetService = shipmentUpdated.targetService();
        this.createdBy = shipmentUpdated.createdBy();
        this.updatedBy = shipmentUpdated.createdBy();
        this.departmentCode = shipmentUpdated.departmentCode();
        this.processType = shipmentUpdated.processType();
        this.serviceType = shipmentUpdated.serviceType();
        this.request = shipmentUpdated.request();
        this.response = shipmentUpdated.response();
        markAsModified();
    }

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

    public void changeDeviceId(final DeviceId deviceId) {
        this.deviceId = deviceId;
        markAsModified();
    }

    public void changeDepartmentCode(final DepartmentCode departmentCode) {
        this.departmentCode = departmentCode;
        markAsModified();
    }

    public void changeCreatedBy(final UserId createdBy) {
        this.createdBy = createdBy;
        markAsModified();
    }

    public void changeUpdatedBy(final UserId updatedBy) {
        this.updatedBy = updatedBy;
        markAsModified();
    }

    public void changeProcessType(final ProcessType processType) {
        this.processType = processType;
        markAsModified();
    }

    public void changeServiceType(final ServiceType serviceType) {
        this.serviceType = serviceType;
        markAsModified();
    }

    public void changeServices(final String sourceService, final String targetService) {
        this.sourceService = sourceService;
        this.targetService = targetService;
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
