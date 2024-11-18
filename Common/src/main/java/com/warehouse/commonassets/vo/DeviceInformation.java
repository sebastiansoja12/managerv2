package com.warehouse.commonassets.vo;

public class DeviceInformation {

     private final String version;

     private final Long terminalId;

     private final String username;

     private final String depotCode;

     public DeviceInformation(String version, Long terminalId, String username, String depotCode) {
          this.version = version;
          this.terminalId = terminalId;
          this.username = username;
          this.depotCode = depotCode;
     }

     public String getVersion() {
          return version;
     }

     public Long getTerminalId() {
          return terminalId;
     }

     public String getUsername() {
          return username;
     }

     public String getDepotCode() {
          return depotCode;
     }
}
