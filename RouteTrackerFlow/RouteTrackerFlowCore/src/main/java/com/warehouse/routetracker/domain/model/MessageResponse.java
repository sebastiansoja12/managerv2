package com.warehouse.routetracker.domain.model;

import lombok.Value;


@Value
public class MessageResponse {
    String id;
    String status;
    String failureReason;

    public boolean isStatusOk() {
        return status.equals("OK");
    }
}
