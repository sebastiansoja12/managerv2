package com.warehouse.logistics.infrastructure.adapter.primary.creator;

import com.warehouse.logistics.application.port.primary.command.LogisticsRequest;
import com.warehouse.logistics.domain.vo.ReturnToken;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Request;
import com.warehouse.logistics.infrastructure.adapter.primary.process.Response;
import static com.warehouse.commonassets.enumeration.ProcessType.RETURN;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;

@Component
public class DeliveryReturnCreator implements DeliveryCreator {

    @Override
    public boolean canHandle(final ProcessType processType) {
        return RETURN.equals(processType);
    }

    @Override
    public Set<LogisticsRequest> create(final Request request, final Response response) {
        final List<DeliveryReturnResponseDetails> deliveryReturnResponseDetails =
                response.getDeliveryReturnResponse().getDeliveryReturnResponseDetails();
        return deliveryReturnResponseDetails.stream()
                .map(returnResponseDetails -> map(request.getProcessType(), returnResponseDetails))
                .collect(Collectors.toSet());
    }

    private LogisticsRequest map(final ProcessType processType,
                                 final DeliveryReturnResponseDetails returnResponseDetails) {
        return new LogisticsRequest(returnResponseDetails.getShipmentId(), null, returnResponseDetails.getDepartmentCode(),
                returnResponseDetails.getSupplierCode(), returnResponseDetails.getDeliveryStatus(),
                processType, new ReturnToken(returnResponseDetails.getReturnToken().value()), null, null);
    }
}
