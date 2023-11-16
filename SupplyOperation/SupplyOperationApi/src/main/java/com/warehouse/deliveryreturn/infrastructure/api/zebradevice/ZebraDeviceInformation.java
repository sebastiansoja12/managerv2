package com.warehouse.deliveryreturn.infrastructure.api.zebradevice;


import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "ZebraDeviceInformation")
@XmlAccessorType(XmlAccessType.FIELD)
public class ZebraDeviceInformation {

    public ZebraDeviceInformation(String version, Long zebraId, String username, String depotCode) {
        this.version = version;
        this.zebraId = zebraId;
        this.username = username;
        this.depotCode = depotCode;
    }

    public ZebraDeviceInformation() {
    }

    @XmlElement(name = "Version")
    private String version;

    @XmlElement(name = "ZebraID")
    private Long zebraId;

    @XmlElement(name = "ResponsibleUser")
    private String username;

    @XmlElement(name = "DepotCode")
    private String depotCode;

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public Long getZebraId() {
        return zebraId;
    }

    public void setZebraId(Long zebraId) {
        this.zebraId = zebraId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getDepotCode() {
        return depotCode;
    }

    public void setDepotCode(String depotCode) {
        this.depotCode = depotCode;
    }
}
