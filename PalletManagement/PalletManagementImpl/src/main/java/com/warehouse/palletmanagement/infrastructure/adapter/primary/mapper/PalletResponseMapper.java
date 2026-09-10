package com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper;

import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletDimensionResponseApi;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletCarrierResponseApi;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.api.dto.PalletScanResponseDto;
import com.warehouse.palletmanagement.api.dto.PalletScanShipmentResultDto;
import com.warehouse.palletmanagement.application.port.primary.result.PalletScanResult;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletHistoryEntryResponseApi;
import com.warehouse.palletmanagement.domain.vo.PalletPage;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;
import com.warehouse.palletmanagement.domain.vo.ShipmentLoadResult;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletPageResponseApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletResponseApi;

import java.util.List;

public class PalletResponseMapper {

    public PalletResponseApi map(final PalletDetails pallet) {
        final PalletWeight grossWeight = pallet.grossWeight();
        final PalletWeight maxGrossWeight = pallet.maxGrossWeight();
        return new PalletResponseApi(
                pallet.palletId().value(),
                pallet.trackingNumber().value(),
                pallet.palletStatus().name(),
                pallet.storageStatus(),
                pallet.shipmentIds().stream().map(ShipmentId::getValue).toList(),
                pallet.empty(),
                pallet.loadable(),
                pallet.blockReason().map(Enum::name).orElse(null),
                grossWeight == null ? null : grossWeight.value(),
                grossWeight == null ? null : grossWeight.unit(),
                maxGrossWeight == null ? null : maxGrossWeight.value(),
                maxGrossWeight == null ? null : maxGrossWeight.unit(),
                pallet.originDepartmentId() == null ? null : pallet.originDepartmentId().getValue(),
                pallet.destinationDepartmentId() == null ? null : pallet.destinationDepartmentId().getValue(),
                pallet.sealNumber(),
                pallet.created(),
                pallet.modified(),
                pallet.attributes().palletType(),
                pallet.attributes().handlingPriority(),
                pallet.attributes().refrigerated(),
                pallet.attributes().dimension() == null ? null : new PalletDimensionResponseApi(
                        pallet.attributes().dimension().length(), pallet.attributes().dimension().width(),
                        pallet.attributes().dimension().height()),
                pallet.assignedCarrier().map(this::map).orElse(null));
    }

    public PalletCarrierResponseApi map(final PalletCarrier carrier) {
        return new PalletCarrierResponseApi(carrier.carrierId().getValue(), carrier.carrierName(),
                carrier.transportType() == null ? null : carrier.transportType().name(),
                carrier.driverId() == null ? null : carrier.driverId().getValue(),
                carrier.driverName(), carrier.driverPhone(),
                carrier.vehicleId() == null ? null : carrier.vehicleId().value(), carrier.vehicleRegistrationNumber(),
                carrier.trailerRegistrationNumber(), carrier.assignedAt(), carrier.assignedBy());
    }

    public PalletPageResponseApi map(final PalletPage page) {
        return new PalletPageResponseApi(page.pallets().stream().map(this::map).toList(), page.page(), page.size(),
                page.totalElements());
    }

    public PalletScanResponseDto map(final PalletScanResult result) {
        final PalletDetails pallet = result.pallet();
        final List<PalletScanShipmentResultDto> shipments = result.shipments().stream()
                .map(this::map)
                .toList();
        return new PalletScanResponseDto(
                pallet.palletId().value(),
                pallet.trackingNumber().value(),
                pallet.palletStatus().name(),
                result.success(),
                result.alreadyLoaded(),
                result.errorCode() == null ? null : result.errorCode().name(),
                result.errorCode() == null ? null : errorMessage(result),
                shipments);
    }

    public List<PalletHistoryEntryResponseApi> mapHistory(final List<PalletHistoryEntry> history) {
        return history.stream()
                .map(entry -> new PalletHistoryEntryResponseApi(
                        entry.eventType().name(),
                        entry.palletStatus() == null ? null : entry.palletStatus().name(),
                        entry.occurredAt(),
                        entry.userId() == null ? null : entry.userId().getValue(),
                        entry.username(),
                        entry.departmentId() == null ? null : entry.departmentId().getValue(),
                        entry.details()))
                .toList();
    }

    public PalletScanResponseDto failure(final String trackingNumber, final String errorCode, final String errorMessage) {
        return new PalletScanResponseDto(null, trackingNumber, null, false, false, errorCode, errorMessage, List.of());
    }

    private PalletScanShipmentResultDto map(final ShipmentLoadResult result) {
        return new PalletScanShipmentResultDto(result.shipmentId(), result.loaded(), result.shipmentStatus(),
                result.errorMessage());
    }

    private String errorMessage(final PalletScanResult result) {
        return switch (result.errorCode()) {
            case PALLET_EMPTY -> "Pallet has no shipments";
            case PALLET_LOCKED -> "Pallet is locked";
            case PALLET_OVERWEIGHT -> "Pallet exceeds maximum weight";
            case PALLET_INVALID_STATUS -> "Pallet cannot be loaded in status " + result.pallet().palletStatus();
            case SHIPMENT_STATUS_UPDATE_FAILED -> "Some shipments could not be updated";
        };
    }
}
