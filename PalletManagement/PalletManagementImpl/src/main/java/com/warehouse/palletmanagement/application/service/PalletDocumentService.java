package com.warehouse.palletmanagement.application.service;

import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.palletmanagement.application.port.secondary.DepartmentServicePort;
import com.warehouse.palletmanagement.application.port.secondary.PalletDocumentRendererServicePort;
import com.warehouse.palletmanagement.application.port.secondary.ShipmentServicePort;
import com.warehouse.palletmanagement.domain.model.PalletDocumentData;
import com.warehouse.palletmanagement.domain.vo.PalletDepartment;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentFile;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentShipment;

import java.time.Instant;
import java.util.List;

public class PalletDocumentService {

    private final ShipmentServicePort shipmentServicePort;
    private final DepartmentServicePort departmentServicePort;
    private final PalletDocumentRendererServicePort palletDocumentRendererServicePort;

    public PalletDocumentService(final ShipmentServicePort shipmentServicePort,
                                 final DepartmentServicePort departmentServicePort,
                                 final PalletDocumentRendererServicePort palletDocumentRendererServicePort) {
        this.shipmentServicePort = shipmentServicePort;
        this.departmentServicePort = departmentServicePort;
        this.palletDocumentRendererServicePort = palletDocumentRendererServicePort;
    }

    public PalletDocumentFile label(final PalletDetails pallet) {
        return new PalletDocumentFile("etykieta-" + fileSafe(pallet) + ".pdf",
                this.palletDocumentRendererServicePort.renderLabel(data(pallet)));
    }

    public PalletDocumentFile transportDocument(final PalletDetails pallet) {
        return new PalletDocumentFile("list-przewozowy-" + fileSafe(pallet) + ".pdf",
                this.palletDocumentRendererServicePort.renderTransportDocument(data(pallet)));
    }

    private PalletDocumentData data(final PalletDetails pallet) {
        final List<PalletDocumentShipment> shipments = pallet.shipmentIds().stream()
                .map(shipmentId -> this.shipmentServicePort.describeShipment(shipmentId)
                        .orElseGet(() -> PalletDocumentShipment.unknown(shipmentId)))
                .toList();
        return new PalletDocumentData(pallet, department(pallet.originDepartmentId()),
                department(pallet.destinationDepartmentId()), shipments, Instant.now());
    }

    private PalletDepartment department(final DepartmentId departmentId) {
        if (departmentId == null) {
            return null;
        }
        return this.departmentServicePort.findDepartment(departmentId)
                .orElseGet(() -> PalletDepartment.unknown(departmentId));
    }

    private static String fileSafe(final PalletDetails pallet) {
        return pallet.trackingNumber().value().replaceAll("[^A-Za-z0-9_-]", "_");
    }
}
