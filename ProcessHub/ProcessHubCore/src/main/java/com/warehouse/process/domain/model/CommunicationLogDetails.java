package com.warehouse.process.domain.model;

import java.util.HashSet;
import java.util.Set;

public class CommunicationLogDetails {
    private Set<CommunicationLog> communicationLogs;

    public Set<CommunicationLog> getCommunicationLogs() {
        if (communicationLogs == null) {
            this.communicationLogs = new HashSet<>();
        }
        return communicationLogs;
    }
}
