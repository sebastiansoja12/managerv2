package com.warehouse.returning.infrastructure.adapter.secondary.entity;

import com.warehouse.returning.infrastructure.adapter.secondary.enumeration.ReturnStatus;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "returning_information")
public class ReturnInformationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "reason", nullable = false)
    private String reason;

    @Column(name = "return_status", nullable = false)
    private ReturnStatus returnStatus;

}
