package com.warehouse.route.domain.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import lombok.Data;
import lombok.Value;
import org.hibernate.annotations.Type;

import java.time.LocalDateTime;
import java.util.UUID;

@Value
@Builder
public class Route {

    @Type(type = "uuid-char")
    UUID id;

    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    LocalDateTime created;

    Long userId;

    Long parcelId;

    Long depotId;

    Long supplierId;

}
