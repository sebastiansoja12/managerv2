package com.warehouse.parcelstate.domain.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RerouteResponse {
    Integer token;
    Parcel parcel;
}
