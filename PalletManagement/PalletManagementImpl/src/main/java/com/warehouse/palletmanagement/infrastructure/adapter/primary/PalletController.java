package com.warehouse.palletmanagement.infrastructure.adapter.primary;

import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletUpdateRequestApi;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentFile;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletCarrierRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletStatusRequestApi;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import com.warehouse.auth.AccessUserControl;
import com.warehouse.palletmanagement.api.dto.PalletScanRequestDto;
import com.warehouse.palletmanagement.api.dto.PalletScanResponseDto;
import com.warehouse.palletmanagement.application.port.primary.PalletPort;
import com.warehouse.palletmanagement.application.port.primary.PalletScanPort;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.commonassets.identificator.PalletId;
import com.warehouse.palletmanagement.domain.vo.PalletSearchCriteria;
import com.warehouse.commonassets.identificator.PalletTrackingNumber;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.CreatePalletRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletPageResponseApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletResponseApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletRequestMapper;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.mapper.PalletResponseMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletDepartmentsRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletShipmentsRequestApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletHistoryEntryResponseApi;
import com.warehouse.palletmanagement.infrastructure.adapter.primary.dto.PalletMaxWeightRequestApi;
import java.util.List;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pallets")
@AccessUserControl
public class PalletController {

    private final PalletPort palletPort;
    private final PalletScanPort palletScanPort;
    private final PalletRequestMapper palletRequestMapper;
    private final PalletResponseMapper palletResponseMapper;

    public PalletController(final PalletPort palletPort,
                            final PalletScanPort palletScanPort,
                            final PalletRequestMapper palletRequestMapper,
                            final PalletResponseMapper palletResponseMapper) {
        this.palletPort = palletPort;
        this.palletScanPort = palletScanPort;
        this.palletRequestMapper = palletRequestMapper;
        this.palletResponseMapper = palletResponseMapper;
    }

    @PostMapping
    public ResponseEntity<PalletResponseApi> createEmptyPallet() {
        final PalletDetails pallet = this.palletPort.createEmptyPallet();
        return ResponseEntity.status(HttpStatus.CREATED).body(this.palletResponseMapper.map(pallet));
    }

    @PostMapping("/shipments")
    public ResponseEntity<PalletResponseApi> createPallet(@RequestBody final CreatePalletRequestApi request) {
        final PalletDetails pallet = this.palletPort.createPallet(this.palletRequestMapper.map(request));
        return ResponseEntity.status(HttpStatus.CREATED).body(this.palletResponseMapper.map(pallet));
    }

    @GetMapping
    public ResponseEntity<PalletPageResponseApi> findPallets(
            @RequestParam(name = "status", required = false) final PalletStatus palletStatus,
            @RequestParam(name = "empty", defaultValue = "false") final boolean onlyEmpty,
            @RequestParam(name = "page", defaultValue = "0") final int page,
            @RequestParam(name = "size", defaultValue = "20") final int size) {
        final PalletSearchCriteria criteria = new PalletSearchCriteria(palletStatus, onlyEmpty, page, size);
        return ResponseEntity.ok(this.palletResponseMapper.map(this.palletPort.findPallets(criteria)));
    }

    @GetMapping("/tracking/{trackingNumber}")
    public ResponseEntity<PalletResponseApi> getPallet(@PathVariable final String trackingNumber) {
        final PalletDetails pallet = this.palletPort.getPallet(new PalletTrackingNumber(trackingNumber));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @GetMapping("/{palletId}")
    public ResponseEntity<PalletResponseApi> getPalletById(@PathVariable final String palletId) {
        return ResponseEntity.ok(this.palletResponseMapper.map(this.palletPort.getPallet(new PalletId(palletId))));
    }

    @PutMapping("/{palletId}/cancel")
    public ResponseEntity<PalletResponseApi> cancelPallet(@PathVariable final String palletId) {
        final PalletDetails pallet = this.palletPort.cancelPallet(new PalletId(palletId));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @PutMapping("/{palletId}/shipments")
    public ResponseEntity<PalletResponseApi> addShipments(@PathVariable final String palletId,
                                                          @RequestBody final PalletShipmentsRequestApi request) {
        final PalletDetails pallet = this.palletPort.addShipments(
                this.palletRequestMapper.map(new PalletId(palletId), request));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @DeleteMapping("/{palletId}/shipments/{shipmentId}")
    public ResponseEntity<PalletResponseApi> removeShipment(@PathVariable final String palletId,
                                                            @PathVariable final Long shipmentId) {
        final PalletDetails pallet = this.palletPort.removeShipment(new PalletId(palletId), new ShipmentId(shipmentId));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @PutMapping("/{palletId}/max-weight")
    public ResponseEntity<PalletResponseApi> defineMaxGrossWeight(@PathVariable final String palletId,
                                                                  @RequestBody final PalletMaxWeightRequestApi request) {
        final PalletDetails pallet = this.palletPort.defineMaxGrossWeight(new PalletId(palletId),
                request.maxGrossWeight());
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @GetMapping("/{palletId}/history")
    public ResponseEntity<List<PalletHistoryEntryResponseApi>> getHistory(@PathVariable final String palletId) {
        return ResponseEntity.ok(this.palletResponseMapper.mapHistory(this.palletPort.getHistory(new PalletId(palletId))));
    }

    @PutMapping("/{palletId}/departments")
    public ResponseEntity<PalletResponseApi> changeDepartments(@PathVariable final String palletId,
                                                               @RequestBody final PalletDepartmentsRequestApi request) {
        final PalletDetails pallet = this.palletPort.changeDepartments(new PalletId(palletId),
                this.palletRequestMapper.map(request));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @PutMapping("/{palletId}")
    public ResponseEntity<PalletResponseApi> updatePallet(@PathVariable final String palletId,
                                                          @RequestBody final PalletUpdateRequestApi request) {
        final PalletDetails pallet = this.palletPort.updatePallet(new PalletId(palletId),
                this.palletRequestMapper.map(request));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @PutMapping("/{palletId}/carrier")
    public ResponseEntity<PalletResponseApi> assignCarrier(@PathVariable final String palletId,
                                                           @RequestBody final PalletCarrierRequestApi request) {
        final PalletDetails pallet = this.palletPort.assignCarrier(new PalletId(palletId),
                this.palletRequestMapper.map(request));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @PutMapping("/{palletId}/status")
    public ResponseEntity<PalletResponseApi> changeStatus(@PathVariable final String palletId,
                                                          @RequestBody final PalletStatusRequestApi request) {
        final PalletDetails pallet = this.palletPort.changeStatus(new PalletId(palletId),
                this.palletRequestMapper.map(request));
        return ResponseEntity.ok(this.palletResponseMapper.map(pallet));
    }

    @GetMapping(value = "/tracking/{trackingNumber}/label", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> label(@PathVariable final String trackingNumber) {
        return pdf(this.palletPort.generateLabel(new PalletTrackingNumber(trackingNumber)));
    }

    @GetMapping(value = "/tracking/{trackingNumber}/documents/transport", produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<byte[]> transportDocument(@PathVariable final String trackingNumber) {
        return pdf(this.palletPort.generateTransportDocument(new PalletTrackingNumber(trackingNumber)));
    }

    private ResponseEntity<byte[]> pdf(final PalletDocumentFile file) {
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, ContentDisposition.inline()
                        .filename(file.fileName())
                        .build()
                        .toString())
                .header(HttpHeaders.CACHE_CONTROL, "no-store")
                .body(file.content());
    }

    @PostMapping("/scan")
    public ResponseEntity<PalletScanResponseDto> scan(@RequestBody final PalletScanRequestDto request) {
        return ResponseEntity.ok(this.palletResponseMapper.map(
                this.palletScanPort.scan(this.palletRequestMapper.map(request))));
    }
}
