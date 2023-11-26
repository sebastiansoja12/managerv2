package com.warehouse.parcelstatuschange.domain.vo;

import lombok.*;

@Value
public class Parcel {

    Long id;

    Status parcelStatus;
}
