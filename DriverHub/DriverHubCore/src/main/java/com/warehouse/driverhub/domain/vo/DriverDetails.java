package com.warehouse.driverhub.domain.vo;

import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.driverhub.domain.model.CarrierDriver;

public record DriverDetails(CarrierId carrierId, String carrierName, boolean carrierActive, CarrierDriver driver) {
}
