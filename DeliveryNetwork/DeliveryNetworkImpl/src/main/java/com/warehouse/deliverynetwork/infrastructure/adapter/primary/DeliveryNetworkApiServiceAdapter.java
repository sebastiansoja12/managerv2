package com.warehouse.deliverynetwork.infrastructure.adapter.primary;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.deliverynetwork.api.DeliveryNetworkApiService;
import com.warehouse.deliverynetwork.api.dto.DepartmentIdDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryNetworkDto;
import com.warehouse.deliverynetwork.api.dto.DeliveryPathDto;
import com.warehouse.deliverynetwork.application.port.primary.DeliveryNetworkPort;
import com.warehouse.deliverynetwork.infrastructure.adapter.primary.mapper.DeliveryNetworkApiMapper;

public class DeliveryNetworkApiServiceAdapter implements DeliveryNetworkApiService {

    private final DeliveryNetworkPort deliveryNetworkPort;

    private final DeliveryNetworkApiMapper deliveryNetworkApiMapper;

    public DeliveryNetworkApiServiceAdapter(
            final DeliveryNetworkPort deliveryNetworkPort,
            final DeliveryNetworkApiMapper deliveryNetworkApiMapper) {
        this.deliveryNetworkPort = deliveryNetworkPort;
        this.deliveryNetworkApiMapper = deliveryNetworkApiMapper;
    }

    @Override
    public DeliveryNetworkDto getCurrentNetwork() {
        return this.deliveryNetworkApiMapper.toDto(this.deliveryNetworkPort.getCurrentNetwork());
    }

    @Override
    public boolean areDirectlyConnected(
            final DepartmentIdDto firstDepartmentId,
            final DepartmentIdDto secondDepartmentId) {
        final DepartmentId firstDepartment = this.deliveryNetworkApiMapper.toModel(firstDepartmentId);
        final DepartmentId secondDepartment = this.deliveryNetworkApiMapper.toModel(secondDepartmentId);
        return this.deliveryNetworkPort.areDirectlyConnected(firstDepartment, secondDepartment);
    }

    @Override
    public DeliveryPathDto findDeliveryPath(
            final DepartmentIdDto sourceDepartmentId,
            final DepartmentIdDto targetDepartmentId) {
        final DepartmentId sourceDepartment = this.deliveryNetworkApiMapper.toModel(sourceDepartmentId);
        final DepartmentId targetDepartment = this.deliveryNetworkApiMapper.toModel(targetDepartmentId);
        return this.deliveryNetworkApiMapper.toDto(
                this.deliveryNetworkPort.findDeliveryPath(sourceDepartment, targetDepartment));
    }
}
