package com.warehouse.terminal.infrastructure.adapter.secondary.entity;


import com.warehouse.terminal.infrastructure.adapter.secondary.enumeration.DeviceType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "device")
public class DeviceEntity {

    @Id
    @Column(name = "device_id", nullable = false)
    private Long deviceId;

    @Column(name = "version", nullable = false)
    private String version;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "depot_code", nullable = false)
    private String depotCode;

    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;



}
