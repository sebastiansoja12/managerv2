package com.warehouse.deliverymissed.domain.port.primary;


import com.warehouse.deliverymissed.domain.port.secondary.DeliveryInstructionServicePort;
import com.warehouse.deliverymissed.domain.port.secondary.RouteLogMissedServicePort;
import com.warehouse.deliverymissed.domain.service.DeliveryMissedService;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedDetail;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedRequest;
import com.warehouse.deliverymissed.domain.vo.DeliveryMissedResponse;

public class DeliveryMissedPortImpl implements DeliveryMissedPort {

    private final DeliveryMissedService deliveryMissedService;

    private final RouteLogMissedServicePort logMissedServicePort;

    private final DeliveryInstructionServicePort deliveryInstructionServicePort;

    public DeliveryMissedPortImpl(final DeliveryMissedService deliveryMissedService,
                                  final RouteLogMissedServicePort logMissedServicePort,
                                  final DeliveryInstructionServicePort deliveryInstructionServicePort) {
        this.deliveryMissedService = deliveryMissedService;
        this.logMissedServicePort = logMissedServicePort;
        this.deliveryInstructionServicePort = deliveryInstructionServicePort;
    }

    @Override
    public DeliveryMissedResponse process(final DeliveryMissedRequest request) {
        final DeliveryMissedDetail deliveryMissedDetail = this.deliveryInstructionServicePort
                .getDeliveryInstruction(null);
        return DeliveryMissedResponse.builder().build();
    }
}
