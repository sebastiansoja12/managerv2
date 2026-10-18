package com.warehouse.pallet.infrastructure.adapter.primary;

import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletUpdateRequestApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletStatusRequestApi;
import com.warehouse.pallet.configuration.identificator.PalletId;
import com.warehouse.pallet.domain.enumeration.PalletStatus;
import com.warehouse.pallet.domain.model.Pallet;
import com.warehouse.pallet.domain.model.PalletPage;
import com.warehouse.pallet.domain.model.PalletSearchCriteria;
import com.warehouse.pallet.domain.model.ShipmentAttachRequest;
import com.warehouse.pallet.domain.vo.UserContext;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.warehouse.pallet.domain.vo.TrackingNumber;
import com.warehouse.pallet.configuration.identificator.ShipmentId;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletHistoryEntryApi;
import java.util.List;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletDepartmentsRequestApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletMaxWeightRequestApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletWeightRequestApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletPageApi;
import com.warehouse.pallet.infrastructure.adapter.primary.dto.PalletShipmentsRequestApi;
import com.warehouse.pallet.infrastructure.adapter.primary.mapper.PalletApiMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.warehouse.pallet.domain.port.primary.PalletPort;

@RestController
@RequestMapping("/pallets")
public class PalletStorageController {

    private final PalletPort palletPort;

    public PalletStorageController(final PalletPort palletPort) {
        this.palletPort = palletPort;
    }

    @PostMapping
    public ResponseEntity<PalletApi> createPallet(@AuthenticationPrincipal final UserContext userContext) {
        final Pallet pallet = this.palletPort.createEmptyPallet(userContext.operatorId(), userContext.departmentId());
        return ResponseEntity.status(HttpStatus.CREATED).body(PalletApiMapper.map(pallet));
    }

    @PostMapping("/shipments")
    public ResponseEntity<PalletApi> createPalletWithShipments(
            @AuthenticationPrincipal final UserContext userContext,
            @RequestBody final PalletShipmentsRequestApi palletShipmentsRequest) {
        final Pallet pallet = this.palletPort.createPalletWithShipments(userContext.operatorId(), userContext.departmentId(),
                PalletApiMapper.mapShipmentIds(palletShipmentsRequest.shipmentIds()));
        return ResponseEntity.status(HttpStatus.CREATED).body(PalletApiMapper.map(pallet));
    }

    @GetMapping
    public ResponseEntity<PalletPageApi> findPallets(
            @AuthenticationPrincipal final UserContext userContext,
            @RequestParam(name = "status", required = false) final PalletStatus palletStatus,
            @RequestParam(name = "empty", defaultValue = "false") final boolean onlyEmpty,
            @RequestParam(name = "page", defaultValue = "0") final int page,
            @RequestParam(name = "size", defaultValue = "20") final int size) {
        final PalletPage palletPage = this.palletPort.findPallets(
                new PalletSearchCriteria(userContext.operatorId(), palletStatus, onlyEmpty, page, size));
        return ResponseEntity.ok(PalletApiMapper.map(palletPage));
    }

    @GetMapping("/{palletId}")
    public ResponseEntity<PalletApi> getPallet(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String palletId) {
        final Pallet pallet = this.palletPort.getPallet(new PalletId(palletId), userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<PalletApi> getPalletByTrackingNumber(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String trackingNumber) {
        final Pallet pallet = this.palletPort.getPalletByTrackingNumber(new TrackingNumber(trackingNumber),
                userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @PutMapping("/{palletId}/shipments")
    public ResponseEntity<PalletApi> attachShipments(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String palletId,
            @RequestBody final PalletShipmentsRequestApi palletShipmentsRequest) {
        final PalletId id = new PalletId(palletId);
        this.palletPort.getPallet(id, userContext.operatorId());
        this.palletPort.attachShipments(new ShipmentAttachRequest(
                PalletApiMapper.mapShipmentIds(palletShipmentsRequest.shipmentIds()), id));
        return ResponseEntity.ok(PalletApiMapper.map(this.palletPort.getPallet(id, userContext.operatorId())));
    }

    @DeleteMapping("/{palletId}/shipments/{shipmentId}")
    public ResponseEntity<PalletApi> removeShipment(@AuthenticationPrincipal final UserContext userContext,
                                                    @PathVariable final String palletId,
                                                    @PathVariable final Long shipmentId) {
        final Pallet pallet = this.palletPort.removeShipment(new PalletId(palletId), new ShipmentId(shipmentId),
                userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @PutMapping("/{palletId}/weight")
    public ResponseEntity<PalletApi> changeGrossWeight(@AuthenticationPrincipal final UserContext userContext,
                                                       @PathVariable final String palletId,
                                                       @RequestBody final PalletWeightRequestApi palletWeightRequest) {
        final Pallet pallet = this.palletPort.changeGrossWeight(new PalletId(palletId),
                PalletApiMapper.mapGrossWeight(palletWeightRequest), userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @PutMapping("/{palletId}/max-weight")
    public ResponseEntity<PalletApi> defineMaxGrossWeight(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String palletId,
            @RequestBody final PalletMaxWeightRequestApi palletMaxWeightRequest) {
        final Pallet pallet = this.palletPort.defineMaxGrossWeight(new PalletId(palletId),
                palletMaxWeightRequest.maxGrossWeight(), userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @PutMapping("/{palletId}")
    public ResponseEntity<PalletApi> updatePallet(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String palletId,
            @RequestBody final PalletUpdateRequestApi request) {
        final Pallet pallet = this.palletPort.updatePallet(new PalletId(palletId), PalletApiMapper.map(request),
                userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @PutMapping("/{palletId}/status")
    public ResponseEntity<PalletApi> changeStatus(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String palletId,
            @RequestBody final PalletStatusRequestApi request) {
        if (request == null || request.palletStatus() == null || request.palletStatus().isBlank()) {
            throw new IllegalArgumentException("Pallet status is required");
        }
        final Pallet pallet = this.palletPort.changeStatus(new PalletId(palletId),
                PalletStatus.valueOf(request.palletStatus().trim()), userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @PutMapping("/{palletId}/departments")
    public ResponseEntity<PalletApi> changeDepartments(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String palletId,
            @RequestBody final PalletDepartmentsRequestApi palletDepartmentsRequest) {
        final Pallet pallet = this.palletPort.changeDepartments(new PalletId(palletId),
                PalletApiMapper.mapDepartmentId(palletDepartmentsRequest.originDepartmentId()),
                PalletApiMapper.mapDepartmentId(palletDepartmentsRequest.destinationDepartmentId()),
                userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }

    @GetMapping("/{palletId}/history")
    public ResponseEntity<List<PalletHistoryEntryApi>> getHistory(@AuthenticationPrincipal final UserContext userContext,
                                                                   @PathVariable final String palletId) {
        return ResponseEntity.ok(this.palletPort.getHistory(new PalletId(palletId), userContext.operatorId()).stream()
                .map(PalletApiMapper::map)
                .toList());
    }

    @PutMapping("/{palletId}/cancel")
    public ResponseEntity<PalletApi> cancelPallet(
            @AuthenticationPrincipal final UserContext userContext,
            @PathVariable final String palletId) {
        final Pallet pallet = this.palletPort.cancelPallet(new PalletId(palletId), userContext.operatorId());
        return ResponseEntity.ok(PalletApiMapper.map(pallet));
    }
}
