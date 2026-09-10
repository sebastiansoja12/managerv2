package com.warehouse.driverhub.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.DriverId;
import com.warehouse.driverhub.application.port.primary.DriverPort;
import com.warehouse.driverhub.domain.vo.DriverChange;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.ActiveRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.DriverRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.DriverResponseApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.FieldValueRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.mapper.CarrierApiMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/drivers")
@AccessUserControl
public class DriverController {

    private final DriverPort driverPort;

    public DriverController(final DriverPort driverPort) {
        this.driverPort = driverPort;
    }

    @GetMapping
    public ResponseEntity<List<DriverResponseApi>> findDrivers(
            @RequestParam(name = "activeOnly", defaultValue = "false") final boolean activeOnly) {
        return ResponseEntity.ok(this.driverPort.findDrivers(activeOnly).stream().map(CarrierApiMapper::map).toList());
    }

    @GetMapping("/{driverId}")
    public ResponseEntity<DriverResponseApi> getDriver(@PathVariable final Long driverId) {
        return ResponseEntity.ok(CarrierApiMapper.map(this.driverPort.getDriver(new DriverId(driverId))));
    }

    @PostMapping
    public ResponseEntity<DriverResponseApi> addDriver(@RequestBody final DriverRequestApi request) {
        final CarrierId carrierId = request == null ? null : CarrierId.of(request.carrierId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CarrierApiMapper.map(this.driverPort.addDriver(carrierId, CarrierApiMapper.map(request))));
    }

    @PutMapping("/{driverId}/first-name")
    public ResponseEntity<DriverResponseApi> changeFirstName(@PathVariable final Long driverId,
                                                             @RequestBody final FieldValueRequestApi request) {
        return change(driverId, new DriverChange.FirstName(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{driverId}/last-name")
    public ResponseEntity<DriverResponseApi> changeLastName(@PathVariable final Long driverId,
                                                            @RequestBody final FieldValueRequestApi request) {
        return change(driverId, new DriverChange.LastName(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{driverId}/phone")
    public ResponseEntity<DriverResponseApi> changePhone(@PathVariable final Long driverId,
                                                         @RequestBody final FieldValueRequestApi request) {
        return change(driverId, new DriverChange.Phone(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{driverId}/license-number")
    public ResponseEntity<DriverResponseApi> changeLicenseNumber(@PathVariable final Long driverId,
                                                                 @RequestBody final FieldValueRequestApi request) {
        return change(driverId, new DriverChange.LicenseNumber(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{driverId}/active")
    public ResponseEntity<DriverResponseApi> changeActive(@PathVariable final Long driverId,
                                                          @RequestBody final ActiveRequestApi request) {
        return change(driverId, new DriverChange.Active(request.active()));
    }

    private ResponseEntity<DriverResponseApi> change(final Long driverId, final DriverChange change) {
        return ResponseEntity.ok(CarrierApiMapper.map(this.driverPort.changeDriver(new DriverId(driverId), change)));
    }
}
