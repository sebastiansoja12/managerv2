package com.warehouse.terminal.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DeviceId;
import jakarta.persistence.*;
import java.io.Serializable;

@MappedSuperclass
public abstract class BaseDeviceEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private DeviceId deviceId;

    public DeviceId getId() {
        return deviceId;
    }

    public void setId(final DeviceId deviceId) {
        this.deviceId = deviceId;
    }
}

