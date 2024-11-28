package com.warehouse.terminal.information;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@XmlRootElement(name = "Device")
@XmlAccessorType(XmlAccessType.FIELD)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "DeviceID")
    private Long deviceId;

    @XmlElement(name = "ResponsibleUser")
    private String username;

    @XmlElement(name = "DepartmentCode")
    private String departmentCode;

    @XmlElement(name = "DeviceUserType")
    private String deviceUserType;

    @XmlElement(name = "DeviceType")
    private String deviceType;

}
