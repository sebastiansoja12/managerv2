package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "deliveryNetwork.DeliveryNetworkEntity")
@Table(
        name = "delivery_network",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_delivery_network_operator",
                columnNames = "operator_id"))
public class DeliveryNetworkEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "delivery_network_id", nullable = false))
    private DeliveryNetworkId deliveryNetworkId;

    @Version
    @Column(name = "version", nullable = false)
    private Long version;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "delivery_network_id", nullable = false)
    private Set<DeliveryNetworkConnectionEntity> connections = new HashSet<>();

    protected DeliveryNetworkEntity() {
    }

    public DeliveryNetworkEntity(
            final DeliveryNetworkId deliveryNetworkId,
            final Set<DeliveryNetworkConnectionEntity> connections) {
        this.deliveryNetworkId = deliveryNetworkId;
        replaceConnections(connections);
    }

    public DeliveryNetworkId getDeliveryNetworkId() {
        return this.deliveryNetworkId;
    }

    public Set<DeliveryNetworkConnectionEntity> getConnections() {
        return Set.copyOf(this.connections);
    }

    public void replaceConnections(final Set<DeliveryNetworkConnectionEntity> connections) {
        this.connections.retainAll(connections);
        this.connections.addAll(connections);
    }

    @PrePersist
    @PreUpdate
    void updateTimestamp() {
        this.updatedAt = Instant.now();
    }
}
