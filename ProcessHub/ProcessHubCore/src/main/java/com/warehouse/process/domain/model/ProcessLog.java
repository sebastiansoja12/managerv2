package com.warehouse.process.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.vo.ShipmentUpdated;

import lombok.Builder;

@Builder
public class ProcessLog {
    private ProcessId processId;
    private String request;
    private String response;
    private Instant createdAt;
    private Instant modifiedAt;
    private CommunicationLogDetails communicationLogDetails;
    private ProcessStatus status;
    private String faultDescription;
    private DeviceInformation deviceInformation;

    public CommunicationLogDetails getCommunicationLogDetails() {
        if (communicationLogDetails == null) {
            communicationLogDetails = new CommunicationLogDetails();
        }
        return communicationLogDetails;
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

    public Instant createdAt() {
        return createdAt;
    }

    public Instant modifiedAt() {
        return modifiedAt;
    }

    public String getFaultDescription() {
        return faultDescription;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public DeviceInformation getDeviceInformation() {
        return deviceInformation;
    }

    public Instant getModifiedAt() {
        return modifiedAt;
    }

    public void saveShipmentUpdated(final ShipmentUpdated shipmentUpdated) {
        final CommunicationLogDetail communicationLogDetail = getCommunicationLogDetails()
                .getCommunicationLogDetail(shipmentUpdated.processType(), shipmentUpdated.serviceType());
        communicationLogDetail.saveShipmentUpdate(shipmentUpdated);
    }

    public void changeResponse(final String response) {
        this.response = response;
        markAsModified();
    }

    public void changeStatus(final ProcessStatus status) {
        this.status = status;
    }

    private void markAsModified() {
        this.modifiedAt = Instant.now();
    }

    public boolean successed() {
        return status != null && status.equals(ProcessStatus.SUCCESS);
    }

    public boolean failed() {
        return status != null && status.equals(ProcessStatus.FAILURE);
    }
}
