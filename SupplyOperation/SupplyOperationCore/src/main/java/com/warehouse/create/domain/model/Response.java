package com.warehouse.create.domain.model;

import lombok.*;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Response {
    private Long terminalId;
    private String version;
    private String username;
    private Long parcelId;
    private UUID processId;
}
