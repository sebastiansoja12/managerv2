package com.warehouse.commonassets.vo;

public class DeviceInformation {

     private final String version;

     private final Long zebraId;

     private final String username;

     private final String depotCode;

     public DeviceInformation(String version, Long zebraId, String username, String depotCode) {
          this.version = version;
          this.zebraId = zebraId;
          this.username = username;
          this.depotCode = depotCode;
     }

     public String getVersion() {
          return version;
     }

     public Long getZebraId() {
          return zebraId;
     }

     public String getUsername() {
          return username;
     }

     public String getDepotCode() {
          return depotCode;
     }
}
