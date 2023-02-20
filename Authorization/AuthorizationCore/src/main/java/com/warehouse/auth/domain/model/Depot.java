package com.warehouse.auth.domain.model;

import lombok.Builder;
import lombok.Data;
import lombok.Value;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Data
@Value
@Builder
public class Depot {

    String city;

    String street;

    String country;

    String depotCode;
}
