package com.warehouse.logisticsdelivery.application.port.primary;

import com.warehouse.commonassets.event.application.port.secondary.DomainEventPublisher;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.service.DestinationCapacityAllocator;
import com.warehouse.logisticsdelivery.domain.event.DeliveryModified;
import com.warehouse.logisticsdelivery.domain.exception.DeliveryModificationException;
import com.warehouse.logisticsdelivery.domain.model.Delivery;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.Objects;

public class DepartmentWorkloadPortImpl implements DepartmentWorkloadPort {

    private static final Logger log = LoggerFactory.getLogger(DepartmentWorkloadPortImpl.class);

    private final DeliveryRepository deliveryRepository;

    private final DomainEventPublisher domainEventPublisher;

    private final DestinationCapacityAllocator destinationCapacityAllocator;

    public DepartmentWorkloadPortImpl(final DeliveryRepository deliveryRepository,
                                      final DomainEventPublisher domainEventPublisher,
                                      final DestinationCapacityAllocator destinationCapacityAllocator) {
        this.deliveryRepository = deliveryRepository;
        this.domainEventPublisher = domainEventPublisher;
        this.destinationCapacityAllocator = destinationCapacityAllocator;
    }

    @Override
    @Transactional(readOnly = true)
    public long countActiveDeliveries(final DepartmentId departmentId) {
        return this.deliveryRepository.countActiveByDepartment(departmentId);
    }

    @Override
    @Transactional
    public void transferActiveDeliveries(final DepartmentId sourceDepartmentId,
                                         final DepartmentId targetDepartmentId) {
        for (final Delivery delivery : this.deliveryRepository.findActiveByDepartment(sourceDepartmentId)) {
            final DepartmentId originDepartmentId = Objects.equals(delivery.getOriginDepartmentId(),
                    sourceDepartmentId) ? targetDepartmentId : delivery.getOriginDepartmentId();
            final DepartmentId destinationDepartmentId = Objects.equals(delivery.getDestinationDepartmentId(),
                    sourceDepartmentId) ? targetDepartmentId : delivery.getDestinationDepartmentId();
            try {
                if (delivery.changeRoute(originDepartmentId, destinationDepartmentId, delivery.getPickupMethod(),
                        delivery.getPickupPointId(), delivery.getDeliveryPickupPointId())) {
                    this.deliveryRepository.createOrUpdate(delivery);
                    this.domainEventPublisher.publish(new DeliveryModified(delivery.snapshot(), Instant.now()));
                }
            } catch (final DeliveryModificationException exception) {
                log.warn("Delivery {} was not transferred from department {} to {}: {}",
                        delivery.getDeliveryId().getId(), sourceDepartmentId.getValue(),
                        targetDepartmentId.getValue(), exception.getMessage());
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<DepartmentLoad> findDepartmentLoads() {
        return this.destinationCapacityAllocator.loads();
    }
}
