package com.warehouse.qrcode.infrastructure.adapter.primary;

import com.warehouse.qrcode.domain.service.ParcelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/v2/api/qrcodes")
@AllArgsConstructor
public class QRCodeGeneratorController {

    private final ParcelService service;

    @GetMapping("/{id}/label")
    public void exportParcelByIdToPdf(HttpServletResponse response, @PathVariable Long id) throws Exception {
        service.exportParcelToPdfById(response, id);
    }
}
