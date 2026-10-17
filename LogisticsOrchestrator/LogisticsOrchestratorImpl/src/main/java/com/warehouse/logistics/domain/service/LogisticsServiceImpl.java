package com.warehouse.logistics.domain.service;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.commonassets.identificator.DeliveryId;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.logistics.domain.enumeration.DeliveryMethod;
import com.warehouse.logistics.domain.enumeration.DeliverySaveStatus;
import com.warehouse.logistics.domain.enumeration.DeliveryType;
import com.warehouse.logistics.domain.model.*;
import com.warehouse.logistics.domain.port.secondary.DeliveryTokenServicePort;
import com.warehouse.logistics.domain.port.secondary.DepartmentRepository;
import com.warehouse.logistics.domain.port.secondary.LogisticsRepository;
import com.warehouse.logistics.infrastructure.adapter.secondary.api.UpdateStatus;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class LogisticsServiceImpl implements LogisticsService {

    private final LogisticsRepository logisticsRepository;

    private final DeliveryTokenServicePort deliveryTokenServicePort;
    private final DepartmentRepository departmentRepository;
    private final OperatorContextProvider operatorContextProvider;

    public LogisticsServiceImpl(final LogisticsRepository logisticsRepository,
                                final DeliveryTokenServicePort deliveryTokenServicePort,
                                final DepartmentRepository departmentRepository,
                                final OperatorContextProvider operatorContextProvider) {
        this.logisticsRepository = logisticsRepository;
        this.deliveryTokenServicePort = deliveryTokenServicePort;
        this.departmentRepository = departmentRepository;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public Set<LogisticsResponse> save(final Set<LogisticsRequest> logisticsRequests) {
        return logisticsRequests.stream()
                .map(this::process)
                .collect(Collectors.toSet());
    }

    private LogisticsResponse process(final LogisticsRequest request) {
        final UserId userId = operatorContextProvider.currentUserId();
        final DepartmentId departmentId = departmentRepository.findIdByCode(request.getDepartmentCode());
        final DeliveryTarget target = request.getTarget();
        final DeliveryType type = determineDeliveryType(request);
        final Delivery delivery = logisticsRepository.findByTargetAndType(target, type)
                .orElseThrow(() -> new RuntimeException("Delivery for shipment: " + request.getShipmentId().toString() + " not found"));

        final String comment = request.getRejectReason() == null ? null : request.getRejectReason().value();
        final String token = request.getDeliveryToken() == null ? null : request.getDeliveryToken().getValue();
        final DeliveryStep deliveryStep = DeliveryStep.attempt(delivery.getDeliveryId(), request.getDeliveryStatus(),
                userId, request.getSupplierId(), departmentId,
                request.getVehicleId(), DeliveryMethod.COURIER, comment, token, delivery.getDeliverySteps().size());
        delivery.addStep(deliveryStep);
        logisticsRepository.createOrUpdate(delivery);
        return new LogisticsResponse(delivery.getDeliveryId(), UpdateStatus.OK, delivery.getShipmentId(),
                DeliverySaveStatus.SAVED, delivery.getTarget());
    }

    private DeliveryType determineDeliveryType(final LogisticsRequest request) {
        return request.getProcessType() == ProcessType.RETURN
                ? DeliveryType.RETURN : DeliveryType.OUTBOUND;
    }

    @Override
    public void createOrUpdate(final Delivery delivery) {
        this.logisticsRepository.createOrUpdate(delivery);
    }

    @Override
    public Optional<Delivery> findByShipmentId(final ShipmentId shipmentId) {
        return this.logisticsRepository.findByShipmentId(shipmentId);
    }

    @Override
    public Optional<Delivery> findById(final DeliveryId deliveryId) {
        return logisticsRepository.findById(deliveryId);
    }

    @Override
    public Optional<Delivery> findByTargetAndType(final DeliveryTarget target, final DeliveryType type) {
        return this.logisticsRepository.findByTargetAndType(target, type);
    }

    @Override
    public List<Delivery> findRecent(final int offset, final int limit) {
        return logisticsRepository.findRecent(offset, limit);
    }
}
