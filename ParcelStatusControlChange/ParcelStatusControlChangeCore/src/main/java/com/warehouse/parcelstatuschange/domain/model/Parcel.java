package com.warehouse.parcelstatuschange.domain.model;

import com.warehouse.parcelstatuschange.domain.enumeration.Status;

import lombok.*;

@Value
public class Parcel {

    Long id;

    Status status;
}
