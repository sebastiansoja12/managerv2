package com.warehouse.create.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TerminalResponse {
    private Long terminalId;
    private String version;
    private String username;
    private Long parcelId;
    private UUID processId;
}
