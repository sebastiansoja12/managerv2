package com.warehouse.logisticsdelivery.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.logisticsdelivery.application.port.secondary.CourierServicePort;
import com.warehouse.logisticsdelivery.application.port.secondary.DeliveryRepository;
import com.warehouse.logisticsdelivery.domain.model.CourierRoster;
import com.warehouse.logisticsdelivery.domain.vo.Courier;
import org.springframework.transaction.support.TransactionOperations;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CourierRosterLoader {

    private final CourierServicePort courierServicePort;

    private final DeliveryRepository deliveryRepository;

    private final TransactionOperations readOnlyTransaction;

    public CourierRosterLoader(final CourierServicePort courierServicePort,
                               final DeliveryRepository deliveryRepository,
                               final TransactionOperations readOnlyTransaction) {
        this.courierServicePort = courierServicePort;
        this.deliveryRepository = deliveryRepository;
        this.readOnlyTransaction = readOnlyTransaction;
    }

    public CourierRoster load(final DepartmentId departmentId) {
        return this.readOnlyTransaction.execute(status -> {
            final List<Courier> couriers = this.courierServicePort.findCouriers(departmentId);
            final Map<SupplierId, Long> activeDeliveries = couriers.stream()
                    .collect(Collectors.toMap(Courier::supplierId,
                            courier -> this.deliveryRepository.countActiveBySupplier(courier.supplierId()),
                            (first, second) -> first));
            return new CourierRoster(couriers, activeDeliveries);
        });
    }
}
