package com.warehouse.create.domain.model;


import com.warehouse.create.domain.enumeration.ProcessType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TerminalRequest {
    private ProcessType processType;
    private DeviceInformation deviceInformation;
    private Long parcelId;
}
