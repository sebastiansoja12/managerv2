package com.warehouse.shipment.infrastructure.adapter.primary;

import com.warehouse.shipment.application.port.primary.ShipmentQueryPort;
import com.warehouse.shipment.domain.vo.ShipmentSearchCriteria;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentPageDto;
import com.warehouse.shipment.infrastructure.adapter.primary.api.ShipmentSearchRequestApi;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentRequestMapper;
import com.warehouse.shipment.infrastructure.adapter.primary.mapper.ShipmentResponseMapper;
import io.micrometer.core.annotation.Counted;
import io.micrometer.core.annotation.Timed;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipments/read-model")
public class ShipmentSearchController {

    private final ShipmentQueryPort shipmentQueryPort;

    private final ShipmentRequestMapper requestMapper;

    private final ShipmentResponseMapper responseMapper;

    public ShipmentSearchController(final ShipmentQueryPort shipmentQueryPort,
                                    final ShipmentRequestMapper requestMapper,
                                    final ShipmentResponseMapper responseMapper) {
        this.shipmentQueryPort = shipmentQueryPort;
        this.requestMapper = requestMapper;
        this.responseMapper = responseMapper;
    }

    @PostMapping("/search")
    @Counted(value = "controller.shipment.list")
    @Timed(value = "controller.shipment.list")
    public ResponseEntity<ShipmentPageDto> search(
            @RequestBody(required = false) final ShipmentSearchRequestApi request) {
        final ShipmentSearchCriteria criteria = requestMapper.map(request);
        final ShipmentPageDto shipmentPage = responseMapper.map(shipmentQueryPort.searchShipments(criteria));
        return ResponseEntity.ok(shipmentPage);
    }
}
