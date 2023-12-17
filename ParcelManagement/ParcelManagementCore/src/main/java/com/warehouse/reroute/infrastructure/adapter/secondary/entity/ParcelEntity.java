package com.warehouse.reroute.infrastructure.adapter.secondary.entity;

import com.warehouse.reroute.domain.enumeration.ParcelType;
import com.warehouse.reroute.domain.enumeration.Size;
import com.warehouse.reroute.domain.enumeration.Status;
import jakarta.persistence.*;
import lombok.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parcel")
@Entity(name = "reroute.ParcelEntity")
public class ParcelEntity {

    @Id
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "sender_telephone", nullable = false)
    private String senderTelephone;

    @Valid
    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Valid
    @Column(name = "sender_city", nullable = false)
    private String senderCity;

    @Valid
    @Column(name = "sender_street", nullable = false)
    private String senderStreet;

    @Valid
    @Column(name = "sender_postal_code", nullable = false)
    private String senderPostalCode;

    @Valid
    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Valid
    @Column(name = "recipient_telephone", nullable = false)
    private String recipientTelephone;

    @Valid
    @Column(name = "recipient_first_name", nullable = false)
    private String recipientFirstName;

    @Valid
    @Column(name = "recipient_last_name", nullable = false)
    private String recipientLastName;

    @Valid
    @Column(name = "recipient_city", nullable = false)
    private String recipientCity;

    @Valid
    @Column(name = "recipient_street", nullable = false)
    private String recipientStreet;

    @Valid
    @Pattern(regexp="\\d{2}-\\d{3}")
    @Column(name = "recipient_postal_code", nullable = false)
    private String recipientPostalCode;

    @Column(name = "parcel_size", nullable = false)
    private Size parcelSize;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "type", nullable = false)
    private ParcelType parcelType;

    @Column(name = "parent_related_id")
    private Long parcelRelatedId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "locked", nullable = false)
    private Boolean locked;
}
