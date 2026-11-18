package com.warehouse.qrcode;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

import com.lowagie.text.Image;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;

public class BarcodeGeneratorServiceImplTest {

    @Test
    public void shouldCreateQrCodeImage() throws Exception {
        // given
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
        final String barcodeText = "INP000000123";

        // when
        final Image resultImage = barcodeGeneratorService.generateQRCodeImage(barcodeText);

        // then
        assertThat(resultImage).isNotNull();
        assertThat(resultImage.getWidth()).isEqualTo(400);
        assertThat(resultImage.getHeight()).isEqualTo(400);
    }
}
