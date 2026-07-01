package com.warehouse.terminal.domain.vo;

import com.warehouse.terminal.domain.enumeration.NetworkType;

public class DeviceNetwork {
    private String ipAddress;
    private String publicIpAddress;
    private NetworkType networkType;
    private String carrier;
    private String simSerial;
    private Boolean roaming;
    private Boolean vpnConnected;
    private String wifiSsid;
    private String bluetoothMac;

    public DeviceNetwork() {}

	public DeviceNetwork(final String bluetoothMac, final String carrier, final String ipAddress,
                         final NetworkType networkType, final String publicIpAddress, final Boolean roaming, final String simSerial,
                         final Boolean vpnConnected, final String wifiSsid) {
        this.bluetoothMac = bluetoothMac;
        this.carrier = carrier;
        this.ipAddress = ipAddress;
        this.networkType = networkType;
        this.publicIpAddress = publicIpAddress;
        this.roaming = roaming;
        this.simSerial = simSerial;
        this.vpnConnected = vpnConnected;
        this.wifiSsid = wifiSsid;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }

    public void setBluetoothMac(final String bluetoothMac) {
        this.bluetoothMac = bluetoothMac;
    }

    public String getCarrier() {
        return carrier;
    }

    public void setCarrier(final String carrier) {
        this.carrier = carrier;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(final String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public void setNetworkType(final NetworkType networkType) {
        this.networkType = networkType;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public void setPublicIpAddress(final String publicIpAddress) {
        this.publicIpAddress = publicIpAddress;
    }

    public Boolean getRoaming() {
        return roaming;
    }

    public void setRoaming(final Boolean roaming) {
        this.roaming = roaming;
    }

    public String getSimSerial() {
        return simSerial;
    }

    public void setSimSerial(final String simSerial) {
        this.simSerial = simSerial;
    }

    public Boolean getVpnConnected() {
        return vpnConnected;
    }

    public void setVpnConnected(final Boolean vpnConnected) {
        this.vpnConnected = vpnConnected;
    }

    public String getWifiSsid() {
        return wifiSsid;
    }

    public void setWifiSsid(final String wifiSsid) {
        this.wifiSsid = wifiSsid;
    }

    public DeviceNetwork update(final DeviceNetwork network) {
        if (network.getIpAddress() != null) {
            this.ipAddress = network.getIpAddress();
        }
        if (network.getPublicIpAddress() != null) {
            this.publicIpAddress = network.getPublicIpAddress();
        }
        if (network.getNetworkType() != null) {
            this.networkType = network.getNetworkType();
        }
        if (network.getCarrier() != null) {
            this.carrier = network.getCarrier();
        }
        if (network.getSimSerial() != null) {
            this.simSerial = network.getSimSerial();
        }
        if (network.getRoaming() != null) {
            this.roaming = network.getRoaming();
        }
        if (network.getVpnConnected() != null) {
            this.vpnConnected = network.getVpnConnected();
        }
        if (network.getWifiSsid() != null) {
            this.wifiSsid = network.getWifiSsid();
        }
        if (network.getBluetoothMac() != null) {
            this.bluetoothMac = network.getBluetoothMac();
        }
        return this;
    }
}
