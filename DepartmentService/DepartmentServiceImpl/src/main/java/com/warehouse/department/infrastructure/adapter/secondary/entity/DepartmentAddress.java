package com.warehouse.department.infrastructure.adapter.secondary.entity;


import com.warehouse.commonassets.enumeration.CountryCode;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

@Embeddable
public record DepartmentAddress(

		@Column(name = "city") String city,

		@Column(name = "postal_code") String postalCode,

		@Column(name = "street") String street,

		@Enumerated(EnumType.STRING) @Column(name = "country_code") CountryCode countryCode

) {
	public DepartmentAddress() {
		this(null, null, null, null);
	}
}
