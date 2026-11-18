package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.application.port.secondary.DepartmentCapacityServicePort;
import com.warehouse.logisticsdelivery.domain.service.DepartmentCapacityBalancer;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentCapacity;
import com.warehouse.logisticsdelivery.domain.vo.DepartmentLoad;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Clock;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class DestinationCapacityAllocator {

    private static final Logger log = LoggerFactory.getLogger(DestinationCapacityAllocator.class);

    private final DeliveryRepository deliveryRepository;

    private final DepartmentCapacityServicePort departmentCapacityServicePort;

    private final DepartmentCapacityBalancer departmentCapacityBalancer;

    private final Clock clock;

    public DestinationCapacityAllocator(final DeliveryRepository deliveryRepository,
                                        final DepartmentCapacityServicePort departmentCapacityServicePort,
                                        final DepartmentCapacityBalancer departmentCapacityBalancer,
                                        final Clock clock) {
        this.deliveryRepository = deliveryRepository;
        this.departmentCapacityServicePort = departmentCapacityServicePort;
        this.departmentCapacityBalancer = departmentCapacityBalancer;
        this.clock = clock;
    }

    public DepartmentId allocate(final DepartmentId resolvedDepartmentId) {
        final List<DepartmentCapacity> departments = this.departmentCapacityServicePort.findDepartments();
        final DepartmentId allocatedDepartmentId = this.departmentCapacityBalancer.allocate(resolvedDepartmentId,
                departments, this::deliveriesToday);
        if (!allocatedDepartmentId.equals(resolvedDepartmentId)) {
            log.info("Department {} reached its daily capacity, delivery redirected to department {}",
                    resolvedDepartmentId.getValue(), allocatedDepartmentId.getValue());
        }
        departments.stream()
                .filter(department -> department.departmentId().equals(allocatedDepartmentId))
                .map(department -> this.departmentCapacityBalancer.load(department,
                        deliveriesToday(department.departmentId()) + 1))
                .filter(DepartmentLoad::warning)
                .findFirst()
                .ifPresent(load -> log.warn("Department {} load is {}% of its daily capacity ({} of {})",
                        load.departmentId().getValue(), load.utilizationPercent(), load.deliveriesToday(),
                        load.dailyCapacity()));
        return allocatedDepartmentId;
    }

    public List<DepartmentLoad> loads() {
        return this.departmentCapacityServicePort.findDepartments().stream()
                .map(department -> this.departmentCapacityBalancer.load(department,
                        deliveriesToday(department.departmentId())))
                .toList();
    }

    private long deliveriesToday(final DepartmentId departmentId) {
        final LocalDate today = LocalDate.now(this.clock);
        final LocalDateTime startOfDay = today.atStartOfDay();
        return this.deliveryRepository.countCreatedForDestinationBetween(departmentId, startOfDay,
                startOfDay.plusDays(1));
    }
}
