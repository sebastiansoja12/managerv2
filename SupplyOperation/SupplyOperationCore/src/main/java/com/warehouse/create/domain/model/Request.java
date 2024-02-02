package com.warehouse.create.domain.model;


import com.warehouse.create.domain.enumeration.ProcessType;
import lombok.*;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Request {
    private ProcessType processType;
    private DeviceInformation deviceInformation;
    private Long parcelId;
}
