package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Size;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.Status;
import com.warehouse.shipment.infrastructure.adapter.secondary.enumeration.ParcelType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import org.hibernate.annotations.Parameter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "parcel")
@Entity(name = "parcel.ParcelEntity")
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
