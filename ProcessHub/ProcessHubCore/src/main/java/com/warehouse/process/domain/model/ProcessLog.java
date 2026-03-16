package com.warehouse.process.domain.model;

import java.time.Instant;

import com.warehouse.commonassets.identificator.ProcessId;
import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.vo.DeviceValidation;
import com.warehouse.process.domain.vo.ProcessCommunication;
import com.warehouse.process.domain.vo.ProcessLogSnapshot;
import com.warehouse.process.domain.vo.ShipmentRejected;
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

    public void applyShipmentUpdate(final ShipmentUpdated shipmentUpdated) {
        final CommunicationLogDetail communicationLogDetail = getCommunicationLogDetails()
                .addCommunicationLogDetail(shipmentUpdated.processType(), shipmentUpdated.serviceType());
        communicationLogDetail.applyShipmentUpdate(shipmentUpdated);
    }

    public void applyDeviceValidation(final DeviceValidation deviceValidation) {
        final CommunicationLogDetail communicationLogDetail = getCommunicationLogDetails()
                .addCommunicationLogDetail(deviceValidation.processType(), deviceValidation.serviceType());
        communicationLogDetail.applyDeviceValidation(deviceValidation);
    }

    public void applyShipmentRejected(final ShipmentRejected shipmentRejected) {
        final CommunicationLogDetail communicationLogDetail = getCommunicationLogDetails()
                .addCommunicationLogDetail(shipmentRejected.processType(), shipmentRejected.serviceType());
        communicationLogDetail.changeRequest(shipmentRejected.request());
        communicationLogDetail.changeResponse(shipmentRejected.response());
        communicationLogDetail.changeFaultDescription(shipmentRejected.faultDescription());
        communicationLogDetail.changeServices(shipmentRejected.serviceType().name(), "SHIPMENT_REJECTION");
    }

    public void applyCommunication(final ProcessCommunication communication) {
        final CommunicationLogDetail communicationLogDetail = getCommunicationLogDetails()
                .addCommunicationLogDetail(communication.processType(), communication.sourceServiceType());
        communicationLogDetail.changeRequest(communication.request());
        communicationLogDetail.changeResponse(communication.response());
        communicationLogDetail.changeFaultDescription(communication.faultDescription());
        communicationLogDetail.changeServices(
                communication.sourceServiceType().name(),
                communication.targetServiceType().name());
    }

    public void changeResponse(final String response) {
        this.response = response;
        markAsModified();
    }

    public void changeStatus(final ProcessStatus status) {
        this.status = status;
        markAsModified();
    }

    public void changeStatus(final ProcessStatus status, final String faultDescription) {
        this.status = status;
        this.faultDescription = faultDescription;
        markAsModified();
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

    public ProcessLogSnapshot snapshot() {
        return new ProcessLogSnapshot(
                this.processId,
                this.request,
                this.response,
                this.createdAt,
                this.modifiedAt,
                this.communicationLogDetails,
                this.status,
                this.faultDescription,
                this.deviceInformation);
    }

    public void applySnapshot(final ProcessLogSnapshot snapshot) {
        this.processId = snapshot.processId();
        this.request = snapshot.request();
        this.response = snapshot.response();
        this.createdAt = snapshot.createdAt();
        this.modifiedAt = snapshot.modifiedAt();
        this.communicationLogDetails = snapshot.communicationLogDetails();
        this.status = snapshot.status();
        this.faultDescription = snapshot.faultDescription();
        this.deviceInformation = snapshot.deviceInformation();
    }
}
