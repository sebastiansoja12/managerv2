package com.warehouse.reroute.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RerouteParcelRequest {

    private Long id;

    private RerouteParcel parcel;

    private Integer token;

}