package com.warehouse.qrcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.Test;

import com.warehouse.qrcode.domain.enumeration.LabelFormat;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.PdfShipmentLabelGenerator;
import com.warehouse.qrcode.domain.service.ShipmentLabelGeneratorFactory;
import com.warehouse.qrcode.domain.service.ZplShipmentLabelGenerator;

class ShipmentLabelGeneratorFactoryTest {

    @Test
    void shouldReturnGeneratorForConfiguredFormat() {
        final ShipmentLabelGeneratorFactory factory = new ShipmentLabelGeneratorFactory(List.of(
                PdfShipmentLabelGenerator.a6(new BarcodeGeneratorServiceImpl()),
                new ZplShipmentLabelGenerator()));

        assertThat(factory.forFormat(LabelFormat.PDF_A6).format()).isEqualTo(LabelFormat.PDF_A6);
        assertThat(factory.forFormat(LabelFormat.ZPL).fileExtension()).isEqualTo("zpl");
    }

    @Test
    void shouldFailForFormatWithoutGenerator() {
        final ShipmentLabelGeneratorFactory factory =
                new ShipmentLabelGeneratorFactory(List.of(new ZplShipmentLabelGenerator()));

        assertThatThrownBy(() -> factory.forFormat(LabelFormat.PDF_A4))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void shouldRejectDuplicatedGenerators() {
        assertThatThrownBy(() -> new ShipmentLabelGeneratorFactory(List.of(
                new ZplShipmentLabelGenerator(), new ZplShipmentLabelGenerator())))
                .isInstanceOf(IllegalStateException.class);
    }
}
