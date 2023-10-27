package com.warehouse.reroute.domain.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RerouteNotification {
    private String subject;
    private String recipient;
    private String body;
}
