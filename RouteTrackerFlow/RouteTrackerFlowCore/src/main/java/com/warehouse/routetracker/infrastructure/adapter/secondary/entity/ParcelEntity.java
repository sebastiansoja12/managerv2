package com.warehouse.routetracker.infrastructure.adapter.secondary.entity;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.ParcelType;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.routetracker.infrastructure.adapter.secondary.enumeration.Status;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parcel")
@Entity(name = "route.ParcelEntity")
@EntityListeners(AuditingEntityListener.class)
public class ParcelEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceParcelIdGenerator")
    @GenericGenerator(
            name = "sequenceParcelIdGenerator",
            strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
            parameters = {
                    @Parameter(name = "sequence_name", value = "message_sequence"),
                    @Parameter(name = "initial_value", value = "1000000000"),
                    @Parameter(name = "increment_size", value = "100")
            }
    )
    private Long id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "sender_telephone", nullable = false)
    private String senderTelephone;

    @Column(name = "sender_email", nullable = false)
    private String senderEmail;

    @Column(name = "sender_city", nullable = false)
    private String senderCity;

    @Column(name = "sender_street", nullable = false)
    private String senderStreet;

    @Column(name = "sender_postal_code", nullable = false)
    private String senderPostalCode;

    @Column(name = "recipient_email", nullable = false)
    private String recipientEmail;

    @Column(name = "recipient_telephone", nullable = false)
    private String recipientTelephone;

    @Column(name = "recipient_first_name", nullable = false)
    private String recipientFirstName;

    @Column(name = "recipient_last_name", nullable = false)
    private String recipientLastName;

    @Column(name = "recipient_city", nullable = false)
    private String recipientCity;

    @Column(name = "recipient_street", nullable = false)
    private String recipientStreet;

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


    public void updateStatus(Status status) {
        this.status = status;
    }
}
