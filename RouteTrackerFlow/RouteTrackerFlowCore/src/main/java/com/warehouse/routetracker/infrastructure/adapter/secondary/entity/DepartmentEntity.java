package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "route.DepartmentEntity")
@Builder
@Table(name = "department")
public class DepartmentEntity {

    @Id
    @Column(name = "department_code", nullable = false)
    private String departmentCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

    @Column(name = "country", nullable = false)
    private String country;

}

