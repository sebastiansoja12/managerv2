package com.warehouse.create.domain.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class DeviceInformation {

     private String version;

     private Long zebraId;

     private String username;

     private String depotCode;
}
