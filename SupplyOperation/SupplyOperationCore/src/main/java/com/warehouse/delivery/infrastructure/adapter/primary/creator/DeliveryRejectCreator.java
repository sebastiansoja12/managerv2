package com.warehouse.delivery.infrastructure.adapter.primary.creator;

import static com.warehouse.commonassets.enumeration.ProcessType.REJECT;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.delivery.domain.model.DeliveryRequest;
import com.warehouse.delivery.domain.model.Request;
import com.warehouse.delivery.domain.model.Response;
import com.warehouse.delivery.domain.vo.RejectReason;
import com.warehouse.deliveryreject.domain.vo.DeliveryRejectResponse;

@Component
public class DeliveryRejectCreator implements DeliveryCreator {

    @Override
    public boolean canHandle(final ProcessType processType) {
        return REJECT.equals(processType);
    }

    @Override
    public Set<DeliveryRequest> create(final Request request, final Response response) {
        final DeliveryRejectResponse deliveryRejectResponse = response.getDeliveryRejectResponse();
        return deliveryRejectResponse.getDeliveryRejectResponseDetails().stream()
                .map(deliveryRejectResponseDetails -> {
                    final RejectReason rejectReason = new RejectReason(deliveryRejectResponseDetails.getRejectReason().getValue());
                    return DeliveryRequest
                            .builder()
                            .deliveryStatus(deliveryRejectResponseDetails.getDeliveryStatus())
                            .rejectReason(rejectReason)
                            .shipmentId(deliveryRejectResponseDetails.getShipmentId())
                            .newShipmentId(deliveryRejectResponseDetails.getNewShipmentId())
                            .departmentCode(deliveryRejectResponseDetails.getDepartmentCode())
                            .supplierCode(deliveryRejectResponseDetails.getSupplierCode())
                            .processType(request.getProcessType())
                            .build();
                })
                .collect(Collectors.toSet());
    }
}
