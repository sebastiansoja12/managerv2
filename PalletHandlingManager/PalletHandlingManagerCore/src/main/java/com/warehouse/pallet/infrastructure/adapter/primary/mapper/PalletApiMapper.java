package com.warehouse.pallet.infrastructure.adapter.primary.mapper;

import com.warehouse.pallet.domain.model.PalletUpdate;
import com.warehouse.pallet.domain.enumeration.PalletType;
import com.warehouse.pallet.domain.enumeration.PalletHandlingPriority;
import com.warehouse.pallet.domain.vo.Dimension;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletUpdateRequestApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.DimensionApi;
import com.warehouse.pallet.domain.vo.SealNumber;
import com.warehouse.pallet.configuration.identificator.DepartmentId;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.domain.enumeration.Unit;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletWeightRequestApi;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletHistoryEntry;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletHistoryEntryApi;
import com.warehouse.pallet.domain.model.Weight;
import com.warehouse.pallet.domain.vo.MaxPalletWeight;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletPageApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.WeightApi;

import java.util.Comparator;
import java.util.List;
import java.util.LinkedHashSet;
import java.util.Set;

public final class PalletApiMapper {

    private PalletApiMapper() {
    }

    public static PalletApi map(final Pallet pallet) {
        final List<Long> shipmentIds = pallet.getShipmentIds().stream()
                .map(ShipmentId::value)
                .sorted(Comparator.naturalOrder())
                .toList();
        return new PalletApi(
                pallet.getPalletId().value(),
                pallet.getTrackingNumber() == null ? null : pallet.getTrackingNumber().value(),
                pallet.getOperatorId() == null ? null : pallet.getOperatorId().value(),
                pallet.getPalletStatus().name(),
                pallet.getStorageStatus().name(),
                shipmentIds,
                pallet.isEmpty(),
                pallet.canBeLoaded(),
                pallet.loadBlockReason().map(Enum::name).orElse(null),
                map(pallet.getPalletWeight()),
                map(pallet.getMaxPalletWeight()),
                pallet.getOriginDepartmentId() == null ? null : pallet.getOriginDepartmentId().value(),
                pallet.getDestinationDepartmentId() == null ? null : pallet.getDestinationDepartmentId().value(),
                pallet.getSealNumber() == null ? null : pallet.getSealNumber().value(),
                pallet.getCreated(),
                pallet.getModified(),
                pallet.getPalletType() == null ? null : pallet.getPalletType().name(),
                pallet.getPalletHandlingPriority() == null ? null : pallet.getPalletHandlingPriority().name(),
                Boolean.TRUE.equals(pallet.isRefrigerated()),
                pallet.getDimension() == null ? null : new DimensionApi(pallet.getDimension().getLength(),
                        pallet.getDimension().getWidth(), pallet.getDimension().getHeight()));
    }

    public static PalletPageApi map(final PalletPage palletPage) {
        return new PalletPageApi(
                palletPage.pallets().stream().map(PalletApiMapper::map).toList(),
                palletPage.page(),
                palletPage.size(),
                palletPage.totalElements());
    }

    public static Set<ShipmentId> mapShipmentIds(final List<Long> shipmentIds) {
        if (shipmentIds == null) {
            return new LinkedHashSet<>();
        }
        final Set<ShipmentId> result = new LinkedHashSet<>();
        for (final Long shipmentId : shipmentIds) {
            if (shipmentId == null) {
                throw new IllegalArgumentException("Shipment id cannot be null");
            }
            result.add(new ShipmentId(shipmentId));
        }
        return result;
    }

    public static PalletHistoryEntryApi map(final PalletHistoryEntry entry) {
        return new PalletHistoryEntryApi(
                entry.eventType().name(),
                entry.palletStatus() == null ? null : entry.palletStatus().name(),
                entry.occurredAt(),
                entry.userId() == null ? null : entry.userId().value(),
                entry.userId() == null ? null : entry.userId().username(),
                entry.departmentId() == null ? null : entry.departmentId().value(),
                entry.details());
    }

    public static Weight mapGrossWeight(final PalletWeightRequestApi palletWeightRequest) {
        if (palletWeightRequest.grossWeight() == null) {
            return null;
        }
        final Unit unit = palletWeightRequest.grossWeightUnit() == null
                ? Unit.KG
                : Unit.valueOf(palletWeightRequest.grossWeightUnit());
        return new Weight(palletWeightRequest.grossWeight(), unit);
    }

    public static PalletUpdate map(final PalletUpdateRequestApi request) {
        if (request == null) {
            throw new IllegalArgumentException("Pallet update is required");
        }
        final boolean anyDimension = request.length() != null || request.width() != null || request.height() != null;
        if (anyDimension && (request.length() == null || request.width() == null || request.height() == null)) {
            throw new IllegalArgumentException("All pallet dimensions are required");
        }
        return new PalletUpdate(
                blank(request.palletType()) ? null : PalletType.valueOf(request.palletType().trim()),
                blank(request.handlingPriority()) ? null : PalletHandlingPriority.valueOf(request.handlingPriority().trim()),
                request.refrigerated(),
                anyDimension ? new Dimension(request.length(), request.width(), request.height()) : null,
                blank(request.sealNumber()) ? null : new SealNumber(request.sealNumber().trim()));
    }

    private static boolean blank(final String value) {
        return value == null || value.isBlank();
    }

    public static DepartmentId mapDepartmentId(final Long departmentId) {
        return departmentId == null ? null : new DepartmentId(departmentId);
    }

    private static WeightApi map(final Weight weight) {
        if (weight == null || weight.getValue() == null) {
            return null;
        }
        return new WeightApi(weight.getValue(), weight.getUnit() == null ? null : weight.getUnit().name());
    }

    private static WeightApi map(final MaxPalletWeight weight) {
        if (weight == null || weight.getValue() == null) {
            return null;
        }
        return new WeightApi(weight.getValue(), weight.getUnit() == null ? null : weight.getUnit().name());
    }
}
