package com.warehouse.csv.infrastructure.adapter.secondary.entity;

import java.math.BigDecimal;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment")
@Entity(name = "csv.ParcelEntity")
@EntityListeners(AuditingEntityListener.class)
public class ParcelEntity {

    @Id
    @Column(name = "shipment_id", nullable = false)
    private Long shipmentId;

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
    @Column(name = "recipient_postal_code", nullable = false)
    private String recipientPostalCode;

    @Column(name = "destination", nullable = false)
    private String destination;

    @Column(name = "shipment_related_id")
    private Long parcelRelatedId;

    @Column(name = "dangerous_good_un_number")
    private String dangerousGoodUnNumber;

    @Column(name = "dangerous_good_proper_shipping_name")
    private String dangerousGoodProperShippingName;

    @Column(name = "dangerous_good_hazard_class")
    private String dangerousGoodHazardClass;

    @Column(name = "dangerous_good_packing_group")
    private String dangerousGoodPackingGroup;

    @Column(name = "dangerous_good_quantity")
    private BigDecimal dangerousGoodQuantity;

    @Column(name = "dangerous_good_quantity_unit")
    private String dangerousGoodQuantityUnit;

    @Column(name = "dangerous_good_package_count")
    private Integer dangerousGoodPackageCount;

    @Column(name = "dangerous_good_packaging_type")
    private String dangerousGoodPackagingType;

    @Column(name = "dangerous_good_regulation_type")
    private String dangerousGoodRegulationType;

    @Column(name = "dangerous_good_transport_mode")
    private String dangerousGoodTransportMode;

    @Column(name = "dangerous_good_emergency_contact_24h")
    private String dangerousGoodEmergencyContact24h;

    @Column(name = "dangerous_good_limited_quantity")
    private Boolean dangerousGoodLimitedQuantity;

    @Column(name = "dangerous_good_marine_pollutant")
    private Boolean dangerousGoodMarinePollutant;

    @Column(name = "dangerous_good_corrosive")
    private Boolean dangerousGoodCorrosive;
}
