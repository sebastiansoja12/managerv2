package com.warehouse.deliveryreturn.domain.model;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DeviceId;

public class DeviceInformation {

     private String version;

     private DeviceId deviceId;

     private String username;

     private DepartmentCode departmentCode;

     public DeviceInformation(final String version,
                              final DeviceId deviceId,
                              final String username,
                              final DepartmentCode departmentCode) {
          this.version = version;
          this.deviceId = deviceId;
          this.username = username;
          this.departmentCode = departmentCode;
     }

     public String getVersion() {
          return version;
     }

     public void setVersion(final String version) {
          this.version = version;
     }

     public DeviceId getDeviceId() {
          return deviceId;
     }

     public void setDeviceId(final DeviceId deviceId) {
          this.deviceId = deviceId;
     }

     public String getUsername() {
          return username;
     }

     public void setUsername(final String username) {
          this.username = username;
     }

     public DepartmentCode getDepartmentCode() {
          return departmentCode;
     }

     public void setDepartmentCode(final DepartmentCode departmentCode) {
          this.departmentCode = departmentCode;
     }
}
