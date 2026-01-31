package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import com.warehouse.terminal.domain.enumeration.NetworkType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public class Network {

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "public_ip_address")
    private String publicIpAddress;

    @Enumerated(EnumType.STRING)
    @Column(name = "network_type")
    private NetworkType networkType;

    @Column(name = "carrier")
    private String carrier;

    @Column(name = "sim_serial")
    private String simSerial;

    @Column(name = "roaming")
    private Boolean roaming;

    @Column(name = "vpn_connected")
    private Boolean vpnConnected;

    @Column(name = "wifi_ssid")
    private String wifiSsid;

    @Column(name = "bluetooth_mac")
    private String bluetoothMac;

    protected Network() {}

    public Network(final String ipAddress,
                   final String publicIpAddress,
                   final NetworkType networkType,
                   final String carrier,
                   final String simSerial,
                   final Boolean roaming,
                   final Boolean vpnConnected,
                   final String wifiSsid,
                   final String bluetoothMac) {
        this.ipAddress = ipAddress;
        this.publicIpAddress = publicIpAddress;
        this.networkType = networkType;
        this.carrier = carrier;
        this.simSerial = simSerial;
        this.roaming = roaming;
        this.vpnConnected = vpnConnected;
        this.wifiSsid = wifiSsid;
        this.bluetoothMac = bluetoothMac;
    }

    public String getBluetoothMac() {
        return bluetoothMac;
    }

    public String getCarrier() {
        return carrier;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public NetworkType getNetworkType() {
        return networkType;
    }

    public String getPublicIpAddress() {
        return publicIpAddress;
    }

    public Boolean getRoaming() {
        return roaming;
    }

    public String getSimSerial() {
        return simSerial;
    }

    public Boolean getVpnConnected() {
        return vpnConnected;
    }

    public String getWifiSsid() {
        return wifiSsid;
    }
}
