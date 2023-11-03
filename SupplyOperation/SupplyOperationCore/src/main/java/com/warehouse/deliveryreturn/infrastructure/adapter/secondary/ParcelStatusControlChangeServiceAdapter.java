package com.warehouse.deliveryreturn.infrastructure.adapter.secondary;

import com.warehouse.deliveryreturn.domain.port.secondary.ParcelStatusControlChangeServicePort;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatus;
import com.warehouse.deliveryreturn.domain.vo.UpdateStatusParcelRequest;
import com.warehouse.deliveryreturn.infrastructure.adapter.secondary.property.ParcelStatusProperty;
import lombok.AllArgsConstructor;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestGatewaySupport;

@AllArgsConstructor
public class ParcelStatusControlChangeServiceAdapter extends RestGatewaySupport
		implements ParcelStatusControlChangeServicePort {

    private final RestClient restClient;

    private final ParcelStatusProperty parcelStatusProperty;

    @Override
    public UpdateStatus updateStatus(UpdateStatusParcelRequest updateStatusParcelRequest) {
        // TODO
        return UpdateStatus.OK;
    }
}
