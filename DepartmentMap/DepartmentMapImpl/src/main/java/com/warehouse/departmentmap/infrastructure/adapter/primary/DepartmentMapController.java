package com.warehouse.departmentmap.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.departmentmap.application.port.primary.DepartmentMapPort;
import com.warehouse.departmentmap.application.port.primary.PalletLocationPort;
import com.warehouse.departmentmap.domain.exception.DepartmentMapException;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.CreateDepartmentMapApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.DepartmentMapApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.DepartmentMapSummaryApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.MovePalletApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.PalletLocationApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.PalletMovementApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.PalletTrackingNumberApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.ReserveLocationApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.SaveDepartmentMapLayoutApiRequest;
import com.warehouse.departmentmap.infrastructure.adapter.primary.api.StorageLocationApiResponse;
import com.warehouse.departmentmap.infrastructure.adapter.primary.mapper.DepartmentMapApiMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/department-maps")
@AccessUserControl
public class DepartmentMapController {

    private final DepartmentMapPort departmentMapPort;
    private final PalletLocationPort palletLocationPort;

    public DepartmentMapController(final DepartmentMapPort departmentMapPort,
                                   final PalletLocationPort palletLocationPort) {
        this.departmentMapPort = departmentMapPort;
        this.palletLocationPort = palletLocationPort;
    }

    @GetMapping
    public ResponseEntity<List<DepartmentMapSummaryApiResponse>> findMaps(@RequestParam final Long departmentId) {
        return ResponseEntity.ok(this.departmentMapPort.findMaps(new DepartmentId(departmentId)).stream()
                .map(DepartmentMapApiMapper::map)
                .toList());
    }

    @PostMapping
    public ResponseEntity<DepartmentMapApiResponse> createMap(@RequestBody final CreateDepartmentMapApiRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(DepartmentMapApiMapper.map(this.departmentMapPort.createMap(DepartmentMapApiMapper.map(request))));
    }

    @GetMapping("/{mapId}")
    public ResponseEntity<DepartmentMapApiResponse> getMap(@PathVariable final String mapId) {
        return ResponseEntity.ok(DepartmentMapApiMapper.map(
                this.departmentMapPort.getMap(DepartmentMapApiMapper.mapId(mapId))));
    }

    @PutMapping("/{mapId}")
    public ResponseEntity<DepartmentMapApiResponse> saveLayout(@PathVariable final String mapId,
                                                               @RequestBody final SaveDepartmentMapLayoutApiRequest request) {
        return ResponseEntity.ok(DepartmentMapApiMapper.map(this.departmentMapPort.saveLayout(
                DepartmentMapApiMapper.mapId(mapId), DepartmentMapApiMapper.map(request))));
    }

    @DeleteMapping("/{mapId}")
    public ResponseEntity<Void> deleteMap(@PathVariable final String mapId) {
        this.departmentMapPort.deleteMap(DepartmentMapApiMapper.mapId(mapId));
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/locations/{locationId}/pallets")
    public ResponseEntity<PalletLocationApiResponse> placePallet(@PathVariable final String locationId,
                                                                 @RequestBody final PalletTrackingNumberApiRequest request) {
        if (request == null) {
            throw DepartmentMapException.invalid("Request body is required");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(DepartmentMapApiMapper.map(
                this.palletLocationPort.placePallet(DepartmentMapApiMapper.locationId(locationId),
                        DepartmentMapApiMapper.pallet(request.palletTrackingNumber()))));
    }

    @PutMapping("/locations/{locationId}/reservation")
    public ResponseEntity<StorageLocationApiResponse> reserveLocation(@PathVariable final String locationId,
                                                                      @RequestBody(required = false) final ReserveLocationApiRequest request) {
        return ResponseEntity.ok(DepartmentMapApiMapper.map(this.palletLocationPort.reserveLocation(
                DepartmentMapApiMapper.locationId(locationId), DepartmentMapApiMapper.map(request))));
    }

    @DeleteMapping("/locations/{locationId}/reservation")
    public ResponseEntity<StorageLocationApiResponse> cancelReservation(@PathVariable final String locationId) {
        return ResponseEntity.ok(DepartmentMapApiMapper.map(
                this.palletLocationPort.cancelReservation(DepartmentMapApiMapper.locationId(locationId))));
    }

    @GetMapping("/pallets")
    public ResponseEntity<List<PalletLocationApiResponse>> searchPallets(@RequestParam final String query) {
        return ResponseEntity.ok(this.palletLocationPort.searchPallets(query).stream()
                .map(DepartmentMapApiMapper::map)
                .toList());
    }

    @GetMapping("/pallets/{palletTrackingNumber}")
    public ResponseEntity<PalletLocationApiResponse> findPalletLocation(@PathVariable final String palletTrackingNumber) {
        return this.palletLocationPort.findPalletLocation(DepartmentMapApiMapper.pallet(palletTrackingNumber))
                .map(DepartmentMapApiMapper::map)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> DepartmentMapException.notFound("PALLET_NOT_PLACED",
                        "Pallet " + palletTrackingNumber + " is not placed on any map"));
    }

    @PostMapping("/pallets/{palletTrackingNumber}/move")
    public ResponseEntity<PalletLocationApiResponse> movePallet(@PathVariable final String palletTrackingNumber,
                                                                @RequestBody final MovePalletApiRequest request) {
        if (request == null) {
            throw DepartmentMapException.invalid("Request body is required");
        }
        return ResponseEntity.ok(DepartmentMapApiMapper.map(this.palletLocationPort.movePallet(
                DepartmentMapApiMapper.pallet(palletTrackingNumber),
                DepartmentMapApiMapper.locationId(request.targetLocationId()))));
    }

    @DeleteMapping("/pallets/{palletTrackingNumber}")
    public ResponseEntity<Void> removePallet(@PathVariable final String palletTrackingNumber) {
        this.palletLocationPort.removePallet(DepartmentMapApiMapper.pallet(palletTrackingNumber));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/pallets/{palletTrackingNumber}/history")
    public ResponseEntity<List<PalletMovementApiResponse>> palletHistory(@PathVariable final String palletTrackingNumber) {
        return ResponseEntity.ok(this.palletLocationPort.palletHistory(
                        DepartmentMapApiMapper.pallet(palletTrackingNumber)).stream()
                .map(DepartmentMapApiMapper::map)
                .toList());
    }
}
