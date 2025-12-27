package com.warehouse.department.infrastructure.adapter.secondary.entity;


import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public record DepartmentCoordinates(

		@Column(name = "latitude") Double latitude,

		@Column(name = "longitude") Double longitude

) {
	public DepartmentCoordinates() {
		this(null, null);
	}
}
