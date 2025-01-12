package com.warehouse.qrcode;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import com.itextpdf.text.BadElementException;
import com.lowagie.text.Image;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;

public class BarcodeGeneratorServiceImplTest {

    @Test
    public void shouldCreateQrCodeImage() throws BadElementException, IOException {
        // given
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
        final Long barcodeText = 123L;

        // when
        final Image resultImage = barcodeGeneratorService.generateQRCodeImage(barcodeText);

        // then
        assertNotNull(resultImage);
    }
}
