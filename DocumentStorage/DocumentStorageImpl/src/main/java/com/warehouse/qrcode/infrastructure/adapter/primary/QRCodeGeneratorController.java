package com.warehouse.qrcode.infrastructure.adapter.primary;

import com.warehouse.qrcode.domain.port.primary.QrCodePort;
import com.warehouse.qrcode.domain.vo.ShipmentId;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/qrcodes")
@AllArgsConstructor
public class QRCodeGeneratorController {

    private final QrCodePort qrCodePort;

    @GetMapping("/{id}")
    public void exportShipmentByIdToPdf(HttpServletResponse response, @PathVariable Long id) throws Exception {
        final ShipmentId shipmentId = new ShipmentId(id);
        qrCodePort.exportShipment(response, shipmentId);
    }
}
