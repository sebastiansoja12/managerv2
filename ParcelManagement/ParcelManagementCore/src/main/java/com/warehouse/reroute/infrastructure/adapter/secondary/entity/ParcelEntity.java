package com.warehouse.reroute.infrastructure.adapter.secondary.entity;

import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import com.warehouse.reroute.infrastructure.adapter.secondary.enumeration.ParcelType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment")
@Entity(name = "reroute.ParcelEntity")
public class ParcelEntity {

    @Id
    @TableGenerator(name = "id", initialValue = 1000000, allocationSize = 100)
    @Column(name = "id", nullable = false, unique = true, updatable = false)
    private Long id;

    @Column(name = "firstName", nullable = false)
    private String firstName;

    @Column(name = "lastName", nullable = false)
    private String lastName;

    @Column(name = "senderTelephone", nullable = false)
    private String senderTelephone;

    @Valid
    @Column(name = "senderEmail", nullable = false)
    private String senderEmail;

    @Valid
    @Column(name = "senderCity", nullable = false)
    private String senderCity;

    @Valid
    @Column(name = "senderStreet", nullable = false)
    private String senderStreet;

    @Valid
    @Pattern(regexp="\\d{2}-\\d{3}")
    @Column(name = "senderPostalCode", nullable = false)
    private String senderPostalCode;

    @Valid
    @Column(name = "recipientEmail", nullable = false)
    private String recipientEmail;

    @Valid
    @Column(name = "recipientTelephone", nullable = false)
    private String recipientTelephone;

    @Valid
    @Column(name = "recipientFirstName", nullable = false)
    private String recipientFirstName;

    @Valid
    @Column(name = "recipientLastName", nullable = false)
    private String recipientLastName;

    @Valid
    @Column(name = "recipientCity", nullable = false)
    private String recipientCity;

    @Valid
    @Column(name = "recipientStreet", nullable = false)
    private String recipientStreet;

    @Valid
    @Pattern(regexp="\\d{2}-\\d{3}")
    @Column(name = "recipientPostalCode", nullable = false)
    private String recipientPostalCode;

    @Column(name = "parcelSize", nullable = false)
    private Size parcelSize;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "type", nullable = false)
    private ParcelType parcelType;

    @Column(name = "parentRelatedId")
    private Long parcelRelatedId;
}
