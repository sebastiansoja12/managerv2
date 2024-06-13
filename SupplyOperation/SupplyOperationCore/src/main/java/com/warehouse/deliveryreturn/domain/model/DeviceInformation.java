package com.warehouse.deliveryreturn.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DeviceInformation {

     private String version;

     private Long terminalId;

     private String username;

     private String depotCode;
}
