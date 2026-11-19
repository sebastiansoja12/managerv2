package com.warehouse.pickuppoint.application.service;

import com.warehouse.pickuppoint.application.port.primary.command.ChangePickupPointStatusCommand;
import com.warehouse.pickuppoint.domain.event.PickupPointChanged;
import com.warehouse.pickuppoint.domain.event.PickupPointClosed;
import com.warehouse.pickuppoint.domain.event.PickupPointDeleted;
import com.warehouse.pickuppoint.domain.event.PickupPointResumed;
import com.warehouse.pickuppoint.domain.event.PickupPointSuspended;
import com.warehouse.pickuppoint.domain.model.PickupPoint;
import com.warehouse.pickuppoint.domain.service.PickupPointStatusChangePolicy;
import com.warehouse.pickuppoint.domain.vo.PickupPointSnapshot;

import java.time.Instant;

public class PickupPointStatusService {

    private final PickupPointDepartmentService pickupPointDepartmentService;

    public PickupPointStatusService(final PickupPointDepartmentService pickupPointDepartmentService) {
        this.pickupPointDepartmentService = pickupPointDepartmentService;
    }

    public void changeStatus(
            final PickupPoint pickupPoint,
            final ChangePickupPointStatusCommand command,
            final Instant timestamp) {
        PickupPointStatusChangePolicy.ensureRequestable(pickupPoint.snapshot().status(), command.targetStatus());
        switch (command.targetStatus()) {
            case ACTIVE -> pickupPoint.resume(
                    this.pickupPointDepartmentService.getRequired(pickupPoint.snapshot().departmentId()).active(),
                    timestamp);
            case SUSPENDED -> pickupPoint.suspend(command.reason(), timestamp);
            case CLOSED -> pickupPoint.close(command.reason(), timestamp);
        }
    }

    public PickupPointChanged statusChangedEvent(
            final PickupPointSnapshot previousSnapshot,
            final PickupPointSnapshot snapshot,
            final Instant timestamp) {
        return switch (snapshot.status()) {
            case ACTIVE -> new PickupPointResumed(previousSnapshot, snapshot, timestamp);
            case SUSPENDED -> new PickupPointSuspended(previousSnapshot, snapshot, timestamp);
            case CLOSED -> new PickupPointClosed(previousSnapshot, snapshot, timestamp);
            case DELETED -> new PickupPointDeleted(previousSnapshot, snapshot, timestamp);
        };
    }
}
