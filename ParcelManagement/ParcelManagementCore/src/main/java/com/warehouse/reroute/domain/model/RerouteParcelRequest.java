package com.warehouse.reroute.domain.model;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RerouteParcelRequest {

    Long id;

    RerouteParcel parcel;

    Integer token;

}