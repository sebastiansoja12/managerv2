package com.warehouse.palletmanagement.infrastructure.adapter.secondary.mapper;

import com.warehouse.palletmanagement.domain.vo.PalletAttributes;
import com.warehouse.palletmanagement.domain.vo.PalletDimension;
import com.warehouse.palletmanagement.domain.vo.PalletUpdate;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingUpdateRequestApi;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.domain.vo.PalletDepartmentsChange;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.palletmanagement.domain.enumeration.PalletHistoryEventType;
import com.warehouse.palletmanagement.domain.vo.PalletHistoryEntry;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingHistoryEntryApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingDepartmentsRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingWeightRequestApi;
import com.warehouse.palletmanagement.domain.enumeration.PalletLoadBlockReason;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.PalletPage;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingPageApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingPalletApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingShipmentsRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.secondary.api.PalletHandlingWeightApi;

import java.util.Arrays;
import java.util.List;

public class PalletHandlingManagerResponseMapper {

    public PalletDetails map(final PalletHandlingPalletApi pallet) {
        if (pallet == null || pallet.palletId() == null || pallet.trackingNumber() == null
                || pallet.palletStatus() == null) {
            throw new IllegalStateException("Pallet service returned incomplete pallet");
        }
        return new PalletDetails(
                new PalletId(pallet.palletId()),
                new PalletTrackingNumber(pallet.trackingNumber()),
                PalletStatus.valueOf(pallet.palletStatus()),
                pallet.storageStatus(),
                pallet.shipmentIds() == null ? List.of() : pallet.shipmentIds().stream().map(ShipmentId::new).toList(),
                pallet.empty(),
                pallet.loadable(),
                pallet.loadBlockReason() == null ? null : PalletLoadBlockReason.valueOf(pallet.loadBlockReason()),
                map(pallet.grossWeight()),
                map(pallet.maxGrossWeight()),
                pallet.originDepartmentId() == null ? null : new DepartmentId(pallet.originDepartmentId()),
                pallet.destinationDepartmentId() == null ? null : new DepartmentId(pallet.destinationDepartmentId()),
                pallet.sealNumber(),
                pallet.created(),
                pallet.modified(),
                new PalletAttributes(pallet.palletType(), pallet.handlingPriority(), pallet.refrigerated(),
                        pallet.dimension() == null ? null : new PalletDimension(pallet.dimension().length(),
                                pallet.dimension().width(), pallet.dimension().height())),
                null);
    }

    public PalletPage map(final PalletHandlingPageApi page) {
        if (page == null) {
            throw new IllegalStateException("Pallet service returned empty page");
        }
        final List<PalletDetails> pallets = page.content() == null
                ? List.of()
                : page.content().stream().map(this::map).toList();
        return new PalletPage(pallets, page.page(), page.size(), page.totalElements());
    }

    public PalletHandlingShipmentsRequestApi map(final List<ShipmentId> shipmentIds) {
        return new PalletHandlingShipmentsRequestApi(shipmentIds.stream().map(ShipmentId::getValue).toList());
    }

    public PalletHandlingWeightRequestApi map(final PalletWeight grossWeight) {
        return new PalletHandlingWeightRequestApi(grossWeight.value(), grossWeight.unit());
    }

    public List<PalletHistoryEntry> map(final PalletHandlingHistoryEntryApi[] history) {
        if (history == null) {
            return List.of();
        }
        return Arrays.stream(history).map(this::map).toList();
    }

    private PalletHistoryEntry map(final PalletHandlingHistoryEntryApi entry) {
        return new PalletHistoryEntry(
                historyEventType(entry.eventType()),
                entry.palletStatus() == null ? null : PalletStatus.valueOf(entry.palletStatus()),
                entry.occurredAt(),
                entry.userId() == null ? null : new UserId(entry.userId()),
                entry.username(),
                entry.departmentId() == null ? null : new DepartmentId(entry.departmentId()),
                entry.details());
    }

    private PalletHistoryEventType historyEventType(final String eventType) {
        return Arrays.stream(PalletHistoryEventType.values())
                .filter(type -> type.name().equals(eventType))
                .findFirst()
                .orElse(PalletHistoryEventType.UNKNOWN);
    }

    public PalletHandlingDepartmentsRequestApi map(final PalletDepartmentsChange departmentsChange) {
        return new PalletHandlingDepartmentsRequestApi(
                departmentsChange.originDepartmentId() == null ? null : departmentsChange.originDepartmentId().getValue(),
                departmentsChange.destinationDepartmentId() == null
                        ? null
                        : departmentsChange.destinationDepartmentId().getValue());
    }

    public PalletHandlingUpdateRequestApi map(final PalletUpdate update) {
        final PalletDimension dimension = update.dimension();
        return new PalletHandlingUpdateRequestApi(update.palletType(), update.handlingPriority(), update.refrigerated(),
                dimension == null ? null : dimension.length(),
                dimension == null ? null : dimension.width(),
                dimension == null ? null : dimension.height(),
                update.sealNumber());
    }

    private PalletWeight map(final PalletHandlingWeightApi weight) {
        return weight == null ? null : new PalletWeight(weight.value(), weight.unit());
    }
}
