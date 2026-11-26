package com.warehouse.logisticsdelivery.application.port.primary.result;

import com.warehouse.logisticsdelivery.domain.model.CourierRoster;
import com.warehouse.logisticsdelivery.domain.vo.AssignmentRunSummary;
import com.warehouse.logisticsdelivery.domain.vo.DeliveryAssignmentCursor;

import java.time.Instant;

public record AssignmentBatch(AssignmentRunSummary summary,
                              DeliveryAssignmentCursor nextCursor,
                              Instant maxEligibleAt,
                              boolean lastBatch,
                              CourierRoster roster) {
}
