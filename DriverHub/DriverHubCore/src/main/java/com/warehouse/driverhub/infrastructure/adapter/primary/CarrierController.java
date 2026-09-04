package com.warehouse.driverhub.infrastructure.adapter.primary;

import com.warehouse.auth.AccessUserControl;
import com.warehouse.commonassets.identificator.CarrierId;
import com.warehouse.commonassets.identificator.VehicleId;
import com.warehouse.driverhub.api.dto.CarrierDto;
import com.warehouse.driverhub.application.port.primary.CarrierPort;
import com.warehouse.driverhub.domain.model.Carrier;
import com.warehouse.driverhub.domain.vo.CarrierChange;
import com.warehouse.driverhub.domain.vo.VehicleChange;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.ActiveRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.CarrierRequestApi;
import com.warehouse.driverhub.infrastructure.adapter.primary.dto.CarrierVehicleRequestApi;
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
@RequestMapping("/carriers")
@AccessUserControl
public class CarrierController {

    private final CarrierPort carrierPort;

    public CarrierController(final CarrierPort carrierPort) {
        this.carrierPort = carrierPort;
    }

    @GetMapping
    public ResponseEntity<List<CarrierDto>> findCarriers(
            @RequestParam(name = "activeOnly", defaultValue = "false") final boolean activeOnly) {
        return ResponseEntity.ok(this.carrierPort.findCarriers(activeOnly).stream().map(CarrierApiMapper::map).toList());
    }

    @GetMapping("/{carrierId}")
    public ResponseEntity<CarrierDto> getCarrier(@PathVariable final Long carrierId) {
        return ok(this.carrierPort.getCarrier(new CarrierId(carrierId)));
    }

    @PostMapping
    public ResponseEntity<CarrierDto> registerCarrier(@RequestBody final CarrierRequestApi request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(CarrierApiMapper.map(this.carrierPort.registerCarrier(CarrierApiMapper.map(request))));
    }

    @PutMapping("/{carrierId}/name")
    public ResponseEntity<CarrierDto> changeName(@PathVariable final Long carrierId,
                                                 @RequestBody final FieldValueRequestApi request) {
        return change(carrierId, new CarrierChange.Name(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{carrierId}/tax-id")
    public ResponseEntity<CarrierDto> changeTaxId(@PathVariable final Long carrierId,
                                                  @RequestBody final FieldValueRequestApi request) {
        return change(carrierId, new CarrierChange.Tax(CarrierApiMapper.taxId(CarrierApiMapper.value(request))));
    }

    @PutMapping("/{carrierId}/phone")
    public ResponseEntity<CarrierDto> changePhone(@PathVariable final Long carrierId,
                                                  @RequestBody final FieldValueRequestApi request) {
        return change(carrierId, new CarrierChange.Phone(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{carrierId}/email")
    public ResponseEntity<CarrierDto> changeEmail(@PathVariable final Long carrierId,
                                                  @RequestBody final FieldValueRequestApi request) {
        return change(carrierId, new CarrierChange.Email(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{carrierId}/default-transport-type")
    public ResponseEntity<CarrierDto> changeDefaultTransportType(@PathVariable final Long carrierId,
                                                                 @RequestBody final FieldValueRequestApi request) {
        return change(carrierId, new CarrierChange.DefaultTransport(
                CarrierApiMapper.transportType(CarrierApiMapper.value(request))));
    }

    @PutMapping("/{carrierId}/active")
    public ResponseEntity<CarrierDto> changeActive(@PathVariable final Long carrierId,
                                                   @RequestBody final ActiveRequestApi request) {
        return change(carrierId, new CarrierChange.Active(request.active()));
    }

    @PostMapping("/{carrierId}/vehicles")
    public ResponseEntity<CarrierDto> addVehicle(@PathVariable final Long carrierId,
                                                 @RequestBody final CarrierVehicleRequestApi request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(CarrierApiMapper.map(
                this.carrierPort.addVehicle(new CarrierId(carrierId), CarrierApiMapper.map(request))));
    }

    @PutMapping("/{carrierId}/vehicles/{vehicleId}/registration-number")
    public ResponseEntity<CarrierDto> changeVehicleRegistration(@PathVariable final Long carrierId,
                                                                @PathVariable final Long vehicleId,
                                                                @RequestBody final FieldValueRequestApi request) {
        return changeVehicle(carrierId, vehicleId, new VehicleChange.RegistrationNumber(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{carrierId}/vehicles/{vehicleId}/vehicle-type")
    public ResponseEntity<CarrierDto> changeVehicleType(@PathVariable final Long carrierId,
                                                        @PathVariable final Long vehicleId,
                                                        @RequestBody final FieldValueRequestApi request) {
        return changeVehicle(carrierId, vehicleId,
                new VehicleChange.Type(CarrierApiMapper.vehicleType(CarrierApiMapper.value(request))));
    }

    @PutMapping("/{carrierId}/vehicles/{vehicleId}/trailer-registration-number")
    public ResponseEntity<CarrierDto> changeTrailerRegistration(@PathVariable final Long carrierId,
                                                                @PathVariable final Long vehicleId,
                                                                @RequestBody final FieldValueRequestApi request) {
        return changeVehicle(carrierId, vehicleId,
                new VehicleChange.TrailerRegistrationNumber(CarrierApiMapper.value(request)));
    }

    @PutMapping("/{carrierId}/vehicles/{vehicleId}/max-load")
    public ResponseEntity<CarrierDto> changeMaxLoad(@PathVariable final Long carrierId,
                                                    @PathVariable final Long vehicleId,
                                                    @RequestBody final FieldValueRequestApi request) {
        return changeVehicle(carrierId, vehicleId,
                new VehicleChange.MaxLoad(CarrierApiMapper.decimal(CarrierApiMapper.value(request))));
    }

    @PutMapping("/{carrierId}/vehicles/{vehicleId}/active")
    public ResponseEntity<CarrierDto> changeVehicleActive(@PathVariable final Long carrierId,
                                                          @PathVariable final Long vehicleId,
                                                          @RequestBody final ActiveRequestApi request) {
        return changeVehicle(carrierId, vehicleId, new VehicleChange.Active(request.active()));
    }

    private ResponseEntity<CarrierDto> change(final Long carrierId, final CarrierChange change) {
        return ok(this.carrierPort.changeCarrier(new CarrierId(carrierId), change));
    }

    private ResponseEntity<CarrierDto> changeVehicle(final Long carrierId, final Long vehicleId,
                                                     final VehicleChange change) {
        return ok(this.carrierPort.changeVehicle(new CarrierId(carrierId), new VehicleId(vehicleId), change));
    }

    private static ResponseEntity<CarrierDto> ok(final Carrier carrier) {
        return ResponseEntity.ok(CarrierApiMapper.map(carrier));
    }
}
