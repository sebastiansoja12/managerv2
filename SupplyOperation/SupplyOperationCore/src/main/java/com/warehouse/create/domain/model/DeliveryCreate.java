package com.warehouse.create.domain.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryCreate {
    private Long terminalId;
    private Long parcelId;
    private String version;
    private String supplierCode;
    private String depotCode;
    private String processId;
}
