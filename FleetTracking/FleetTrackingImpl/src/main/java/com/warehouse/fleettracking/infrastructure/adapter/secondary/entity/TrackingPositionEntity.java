package com.warehouse.fleettracking.infrastructure.adapter.secondary.entity;

import com.warehouse.commonassets.identificator.TrackingPositionId;
import com.warehouse.commonassets.identificator.TrackingSessionId;
import com.warehouse.commonassets.model.BelongsToOperator;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity(name = "fleettracking.TrackingPositionEntity")
@Table(name = "tracking_position")
public class TrackingPositionEntity extends BelongsToOperator {

    @EmbeddedId
    @AttributeOverride(name = "value", column = @Column(name = "id"))
    private TrackingPositionId trackingPositionId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "tracking_session_id", nullable = false))
    private TrackingSessionId trackingSessionId;

    @Embedded
    private PositionEmbeddable position;

    public TrackingPositionEntity(final TrackingPositionId trackingPositionId,
                                  final TrackingSessionId trackingSessionId,
                                  final PositionEmbeddable position) {
        this.trackingPositionId = trackingPositionId;
        this.trackingSessionId = trackingSessionId;
        this.position = position;
    }
}
