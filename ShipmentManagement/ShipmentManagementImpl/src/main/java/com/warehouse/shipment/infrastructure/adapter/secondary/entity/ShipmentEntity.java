package com.warehouse.shipment.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.enumeration.CancellationReason;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.enumeration.ShipmentStatus;
import com.warehouse.commonassets.enumeration.ShipmentType;
import com.warehouse.commonassets.identificator.*;
import com.warehouse.commonassets.model.BelongsToOperator;
import com.warehouse.commonassets.model.Money;
import com.warehouse.shipment.domain.enumeration.DeliveryMethod;
import com.warehouse.shipment.domain.enumeration.PickupMethod;
import com.warehouse.shipment.domain.enumeration.PackagingType;
import com.warehouse.shipment.domain.vo.conf.ShipmentServiceLevel;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.envers.Audited;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "shipment")
@Entity(name = "shipment.ShipmentEntity")
@EntityListeners(AuditingEntityListener.class)
@Audited
public class ShipmentEntity extends BelongsToOperator {

    @Column(name = "shipment_id")
    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "shipment_id"))
    private ShipmentId shipmentId;

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "last_name", nullable = false)),
            @AttributeOverride(name = "email", column = @Column(name = "sender_email", nullable = false)),
            @AttributeOverride(name = "telephoneNumber", column = @Column(name = "sender_telephone", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "sender_city", nullable = false)),
            @AttributeOverride(name = "street", column = @Column(name = "sender_street", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "sender_postal_code", nullable = false)),
            @AttributeOverride(name = "countryCode", column = @Column(name = "sender_country_code"))
    })
    private PartyEntity sender;

    @Valid
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "firstName", column = @Column(name = "recipient_first_name", nullable = false)),
            @AttributeOverride(name = "lastName", column = @Column(name = "recipient_last_name", nullable = false)),
            @AttributeOverride(name = "email", column = @Column(name = "recipient_email", nullable = false)),
            @AttributeOverride(name = "telephoneNumber", column = @Column(name = "recipient_telephone", nullable = false)),
            @AttributeOverride(name = "city", column = @Column(name = "recipient_city", nullable = false)),
            @AttributeOverride(name = "street", column = @Column(name = "recipient_street", nullable = false)),
            @AttributeOverride(name = "postalCode", column = @Column(name = "recipient_postal_code", nullable = false)),
            @AttributeOverride(name = "countryCode", column = @Column(name = "recipient_country_code"))
    })
    private PartyEntity recipient;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "target_department_id", nullable = false))
    private DepartmentId targetDepartmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "origin_department_id"))
    private DepartmentId originDepartmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "pickup_point_id", columnDefinition = "UUID"))
    private PickupPointId pickupPointId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "delivery_pickup_point_id", columnDefinition = "UUID"))
    private PickupPointId deliveryPickupPointId;

    @Column(name = "pickup_method")
    @Enumerated(EnumType.STRING)
    private PickupMethod pickupMethod;

    @Column(name = "delivery_method")
    @Enumerated(EnumType.STRING)
    private DeliveryMethod deliveryMethod;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentStatus shipmentStatus;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentType shipmentType;

    @Column(name = "shipment_related_id")
    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "shipment_related_id"))
    private ShipmentId shipmentRelatedId;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Embedded
    private DimensionsEntity dimensions;

    @Embedded
    private WeightEntity weight;

    @Column(name = "customer_reference")
    private String customerReference;

    @Column(name = "content_description", length = 1000)
    private String contentDescription;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "declared_value_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "declared_value_currency"))
    })
    private Money declaredValue;

    @Column(name = "accepted_at")
    private LocalDateTime acceptedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason")
    @Enumerated(EnumType.STRING)
    private CancellationReason cancellationReason;

    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @Column(name = "shipment_priority", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentPriority shipmentPriority;

    @Column(name = "service_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private ShipmentServiceLevel serviceLevel;

    @Column(name = "packaging_type")
    @Enumerated(EnumType.STRING)
    private PackagingType packagingType;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "amount", column = @Column(name = "price_amount")),
            @AttributeOverride(name = "currency", column = @Column(name = "price_currency"))
    })
    private Money price;

    @Column(name = "signature_required", nullable = false)
    private Boolean signatureRequired;

    @Column(name = "external_id", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "external_id"))
    private ExternalId<String> externalId;

    @Column(name = "tracking_number", nullable = false)
    @AttributeOverride(name = "value", column = @Column(name = "tracking_number"))
    private TrackingNumber trackingNumber;
}
