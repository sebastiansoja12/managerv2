package com.warehouse.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DepotDto {
	String city;

	String street;

	String country;

	String depotCode;

	CoordinatesDto coordinates;
}
