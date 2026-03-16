package com.warehouse.logistics.infrastructure.adapter.primary.creator;

import static com.warehouse.commonassets.enumeration.ProcessType.RETURN;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.warehouse.commonassets.enumeration.ProcessType;
import com.warehouse.deliveryreturn.domain.vo.DeliveryReturnResponseDetails;
import com.warehouse.logistics.domain.model.LogisticsRequest;
import com.warehouse.logistics.domain.model.Request;
import com.warehouse.logistics.domain.model.Response;

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
                .map(returnResponseDetails -> LogisticsRequest.from(
                        request.getProcessType(), returnResponseDetails
                ))
                .collect(Collectors.toSet());
    }
}
