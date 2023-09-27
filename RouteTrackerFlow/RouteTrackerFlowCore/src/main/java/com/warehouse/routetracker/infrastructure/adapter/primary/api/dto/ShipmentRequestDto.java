package com.warehouse.routetracker.infrastructure.adapter.primary.api.dto;

import lombok.Data;

@Data
// refactor zeby cala przesylka tu trafila, zeby do eventu przekazac cala paczke tuz po zapisaniu
public class ShipmentRequestDto {

    Long parcelId;
}
