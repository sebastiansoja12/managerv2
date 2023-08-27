package com.warehouse.route.domain.model;

import java.time.LocalDateTime;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Route {

    UUID id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime created;

    Long userId;

    Long parcelId;

    Long depotId;

    String supplierCode;

}
