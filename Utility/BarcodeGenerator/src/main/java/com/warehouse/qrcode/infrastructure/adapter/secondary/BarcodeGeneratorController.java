package com.warehouse.qrcode.infrastructure.adapter.secondary;

import com.warehouse.qrcode.domain.service.ParcelService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;

@RestController()
@RequestMapping("/barcode")
@AllArgsConstructor
public class BarcodeGeneratorController {

    private final ParcelService service;

    @GetMapping("/{id}/label")
    public void exportParcelByIdToPdf(HttpServletResponse response, @PathVariable Long id) throws Exception {
        service.exportParcelToPdfById(response, id);
    }
}