package com.warehouse.route.domain.vo;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SupplyInformation {
    String username;
    Long parcelId;
    LocalDateTime created;
    String depotCode;
    Long supplierId;


    public LocalDateTime created() {
        return this.created = LocalDateTime.now();
    }
}
