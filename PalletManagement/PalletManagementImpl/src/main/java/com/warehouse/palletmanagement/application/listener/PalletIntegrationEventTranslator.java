package com.warehouse.palletmanagement.application.listener;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.api.event.PalletDepartmentsChangedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletLoadedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletLoadingRevertedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.PalletUnloadedIntegrationEvent;
import com.warehouse.palletmanagement.api.event.snapshot.PalletDepartmentsChangedEventData;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadedEventData;
import com.warehouse.palletmanagement.api.event.snapshot.PalletLoadingRevertedEventData;
import com.warehouse.palletmanagement.api.event.snapshot.PalletUnloadedEventData;
import com.warehouse.palletmanagement.domain.event.PalletDepartmentsChanged;
import com.warehouse.palletmanagement.domain.event.PalletLoaded;
import com.warehouse.palletmanagement.domain.event.PalletLoadingReverted;
import com.warehouse.palletmanagement.domain.event.PalletUnloaded;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChangedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletLoadedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletLoadingRevertedSnapshot;
import com.warehouse.palletmanagement.domain.vo.PalletUnloadedSnapshot;
import com.warehouse.eventoutbox.integration.annotation.IntegrationEventTranslation;
import org.springframework.stereotype.Component;

@Component
public class PalletIntegrationEventTranslator {

    @IntegrationEventTranslation
    public PalletLoadedIntegrationEvent translate(final PalletLoaded event) {
        final PalletLoadedSnapshot snapshot = event.getSnapshot();
        final PalletLoadedEventData eventData = new PalletLoadedEventData(
                snapshot.palletId().value(),
                snapshot.trackingNumber().value(),
                snapshot.shipmentIds().stream().map(ShipmentId::getValue).toList(),
                departmentValue(snapshot.originDepartmentId()),
                departmentValue(snapshot.destinationDepartmentId()));
        return new PalletLoadedIntegrationEvent(eventData);
    }

    @IntegrationEventTranslation
    public PalletLoadingRevertedIntegrationEvent translate(final PalletLoadingReverted event) {
        final PalletLoadingRevertedSnapshot snapshot = event.getSnapshot();
        final PalletLoadingRevertedEventData eventData = new PalletLoadingRevertedEventData(
                snapshot.palletId().value(),
                snapshot.trackingNumber().value(),
                snapshot.shipmentIds().stream().map(ShipmentId::getValue).toList());
        return new PalletLoadingRevertedIntegrationEvent(eventData);
    }

    @IntegrationEventTranslation
    public PalletUnloadedIntegrationEvent translate(final PalletUnloaded event) {
        final PalletUnloadedSnapshot snapshot = event.getSnapshot();
        final PalletUnloadedEventData eventData = new PalletUnloadedEventData(
                snapshot.palletId(),
                snapshot.trackingNumber(),
                snapshot.unloadedShipmentIds(),
                snapshot.missingShipmentIds(),
                snapshot.unloadDepartmentId());
        return new PalletUnloadedIntegrationEvent(eventData);
    }

    @IntegrationEventTranslation
    public PalletDepartmentsChangedIntegrationEvent translate(final PalletDepartmentsChanged event) {
        final PalletDepartmentsChangedSnapshot snapshot = event.getSnapshot();
        final PalletDepartmentsChangedEventData eventData = new PalletDepartmentsChangedEventData(
                snapshot.palletId(),
                snapshot.trackingNumber(),
                snapshot.originDepartmentId(),
                snapshot.destinationDepartmentId());
        return new PalletDepartmentsChangedIntegrationEvent(eventData);
    }

    private static Long departmentValue(final DepartmentId departmentId) {
        return departmentId == null ? null : departmentId.value();
    }
}
