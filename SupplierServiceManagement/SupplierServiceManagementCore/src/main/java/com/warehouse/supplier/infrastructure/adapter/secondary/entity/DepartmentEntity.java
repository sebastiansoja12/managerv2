package com.warehouse.supplier.infrastructure.adapter.secondary.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;



@Entity(name = "supplier.DepartmentEntity")
@Table(name = "department")
public class DepartmentEntity {

    @Id
    @Column(name = "department_code", nullable = false, unique = true)
    private String departmentCode;

    @Column(name = "city", nullable = false)
    private String city;

    @Column(name = "street", nullable = false)
    private String street;

}

