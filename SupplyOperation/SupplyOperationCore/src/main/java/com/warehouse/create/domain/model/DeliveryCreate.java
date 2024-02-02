package com.warehouse.create.domain.model;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeliveryCreate {
    private Long terminalId;
    private Long parcelId;
    private String version;
    private String supplierCode;
    private String depotCode;
    private String processId;
}
