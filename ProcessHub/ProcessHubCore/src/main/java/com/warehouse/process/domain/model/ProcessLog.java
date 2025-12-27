package com.warehouse.process.domain.model;

import java.time.Instant;

import com.warehouse.process.domain.enumeration.ProcessStatus;
import com.warehouse.process.domain.vo.ProcessId;

import lombok.Builder;

@Builder
public class ProcessLog {
    private ProcessId processId;
    private String request;
    private String response;
    private Instant timestamp;
    private CommunicationLogDetails communicationLogDetails;
    private ProcessStatus status;

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

    public Instant getTimestamp() {
        return timestamp;
    }
}
