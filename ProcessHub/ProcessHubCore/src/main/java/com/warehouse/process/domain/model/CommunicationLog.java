package com.warehouse.process.domain.model;

import com.warehouse.process.domain.vo.CommunicationLogId;

public class CommunicationLog {
    private CommunicationLogId communicationLogId;
    private String sourceService;
    private String targetService;
    private String request;
    private String response;
}
