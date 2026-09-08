package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.logisticsdelivery.application.port.primary.result.DeliveryHistoryEntry;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryHistoryRepository;
import com.warehouse.logisticsdelivery.domain.enumeration.DeliveryHistoryEventType;
import com.warehouse.logisticsdelivery.domain.event.DeliveryAttemptRecorded;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCanceled;
import com.warehouse.logisticsdelivery.domain.event.DeliveryChanged;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCompleted;
import com.warehouse.logisticsdelivery.domain.event.DeliveryCreated;
import com.warehouse.logisticsdelivery.domain.vo.DeliverySnapshot;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public class DeliveryHistoryPortImpl implements DeliveryHistoryPort {

    private final DeliveryHistoryRepository deliveryHistoryRepository;

    private final OperatorContextProvider operatorContextProvider;

    public DeliveryHistoryPortImpl(final DeliveryHistoryRepository deliveryHistoryRepository,
                                   final OperatorContextProvider operatorContextProvider) {
        this.deliveryHistoryRepository = deliveryHistoryRepository;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public void record(final DeliveryChanged event) {
        final DeliverySnapshot snapshot = event.getSnapshot();
        final UserId userId = this.operatorContextProvider.currentContext().map(OperatorDetails::userId).orElse(null);
        this.deliveryHistoryRepository.save(new DeliveryHistoryEntry(snapshot.deliveryId(), eventType(event),
                event.getTimestamp(), userId, snapshot.deliveryStatus(), snapshot.status(),
                snapshot.originDepartmentId(), snapshot.destinationDepartmentId(), snapshot.pickupMethod(),
                snapshot.method(), snapshot.pickupPointId(), snapshot.deliveryPickupPointId(), snapshot.supplierId(),
                snapshot.vehicleId(), snapshot.signatureRequired(), snapshot.instructions(), snapshot.plannedAt(),
                snapshot.deliverySteps().size()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DeliveryHistoryEntry> findHistory(final DeliveryId deliveryId) {
        return this.deliveryHistoryRepository.findByDeliveryId(deliveryId);
    }

    private DeliveryHistoryEventType eventType(final DeliveryChanged event) {
        if (event instanceof DeliveryCreated) {
            return DeliveryHistoryEventType.CREATED;
        }
        if (event instanceof DeliveryAttemptRecorded) {
            return DeliveryHistoryEventType.ATTEMPT_RECORDED;
        }
        if (event instanceof DeliveryCompleted) {
            return DeliveryHistoryEventType.COMPLETED;
        }
        if (event instanceof DeliveryCanceled) {
            return DeliveryHistoryEventType.CANCELED;
        }
        return DeliveryHistoryEventType.MODIFIED;
    }
}
