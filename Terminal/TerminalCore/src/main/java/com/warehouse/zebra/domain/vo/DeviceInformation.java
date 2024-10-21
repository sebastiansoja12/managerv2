package com.warehouse.zebra.domain.vo;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class DeviceInformation {

     String version;

     Long zebraId;

     String username;

     String depotCode;
}
