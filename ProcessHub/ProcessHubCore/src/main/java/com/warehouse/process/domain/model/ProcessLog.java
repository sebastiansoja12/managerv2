package com.warehouse.process.domain.model;

import java.time.Instant;

import com.warehouse.process.domain.vo.ProcessId;

public class ProcessLog {
    private ProcessId processId;
    private String request;
    private String response;
    private Instant timestamp;
    private CommunicationLogDetails communicationLogDetails;
}
