package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.DepartmentId;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity(name = "deliveryNetwork.DeliveryNetworkConnectionEntity")
@Table(name = "delivery_network_connection")
public class DeliveryNetworkConnectionEntity {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "connection_id", nullable = false))
    private DeliveryNetworkConnectionId connectionId;

    @Embedded
    @AttributeOverride(
            name = "value",
            column = @Column(name = "delivery_network_id", nullable = false, insertable = false, updatable = false))
    private DeliveryNetworkId deliveryNetworkId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "first_department_id", nullable = false))
    private DepartmentId firstDepartmentId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "second_department_id", nullable = false))
    private DepartmentId secondDepartmentId;

    protected DeliveryNetworkConnectionEntity() {
    }

    public DeliveryNetworkConnectionEntity(
            final DeliveryNetworkId deliveryNetworkId,
            final DepartmentId firstDepartmentId,
            final DepartmentId secondDepartmentId) {
        this.connectionId = DeliveryNetworkConnectionId.generate();
        this.deliveryNetworkId = deliveryNetworkId;
        this.firstDepartmentId = firstDepartmentId;
        this.secondDepartmentId = secondDepartmentId;
    }

    public DeliveryNetworkConnectionId getConnectionId() {
        return this.connectionId;
    }

    public DeliveryNetworkId getDeliveryNetworkId() {
        return this.deliveryNetworkId;
    }

    public DepartmentId getFirstDepartmentId() {
        return this.firstDepartmentId;
    }

    public DepartmentId getSecondDepartmentId() {
        return this.secondDepartmentId;
    }

    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (!(object instanceof DeliveryNetworkConnectionEntity that)) {
            return false;
        }
        return Objects.equals(this.deliveryNetworkId, that.deliveryNetworkId)
                && Objects.equals(this.firstDepartmentId, that.firstDepartmentId)
                && Objects.equals(this.secondDepartmentId, that.secondDepartmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.deliveryNetworkId, this.firstDepartmentId, this.secondDepartmentId);
    }
}
