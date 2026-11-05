package com.warehouse.qrcode;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletResponse;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import com.warehouse.commonassets.enumeration.ShipmentPriority;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.qrcode.domain.enumeration.LabelFormat;
import com.warehouse.qrcode.domain.model.Shipment;
import com.warehouse.qrcode.domain.port.primary.QrCodePortImpl;
import com.warehouse.qrcode.domain.port.secondary.LabelConfigurationServicePort;
import com.warehouse.qrcode.domain.port.secondary.ShipmentServicePort;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorService;
import com.warehouse.qrcode.domain.service.BarcodeGeneratorServiceImpl;
import com.warehouse.qrcode.domain.service.PdfShipmentLabelGenerator;
import com.warehouse.qrcode.domain.service.ShipmentLabelGeneratorFactory;
import com.warehouse.qrcode.domain.service.ZplShipmentLabelGenerator;
import com.warehouse.qrcode.domain.vo.LabelParty;
import com.warehouse.qrcode.domain.vo.ParcelDimensions;
import com.warehouse.qrcode.domain.vo.ShipmentId;

@ExtendWith(MockitoExtension.class)
public class QrCodePortImplTest {

    @Mock
    private ShipmentServicePort shipmentServicePort;

    @Mock
    private LabelConfigurationServicePort labelConfigurationServicePort;

    private QrCodePortImpl qrCodePort;

    @BeforeEach
    void setup() {
        final BarcodeGeneratorService barcodeGeneratorService = new BarcodeGeneratorServiceImpl();
        final ShipmentLabelGeneratorFactory factory = new ShipmentLabelGeneratorFactory(List.of(
                PdfShipmentLabelGenerator.a6(barcodeGeneratorService),
                PdfShipmentLabelGenerator.a4(barcodeGeneratorService),
                new ZplShipmentLabelGenerator()));
        qrCodePort = new QrCodePortImpl(shipmentServicePort, labelConfigurationServicePort, factory);
    }

    @Test
    void shouldExportLabelWithOperationalData() throws Exception {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        doReturn(LabelFormat.PDF_A6).when(labelConfigurationServicePort).currentLabelFormat();
        doReturn(buildShipment()).when(shipmentServicePort).find(shipmentId);

        // when
        qrCodePort.exportShipment(response, shipmentId);

        // then
        final String text = labelText(response);
        assertThat(response.getContentType()).isEqualTo("application/pdf");
        assertThat(response.getHeader("Content-Disposition")).endsWith(".pdf");
        assertThat(text).contains("INP000000001", "30-000", "KRAKÓW", "A. N.", "J. K.", "Katowice",
                "2.5 kg", "30 x 20 x 10 cm", "HIGH", "EXPRESS");
        verify(shipmentServicePort).find(shipmentId);
    }

    @Test
    void shouldExportLabelWithoutOptionalData() throws Exception {
        // given
        final ShipmentId shipmentId = new ShipmentId(2L);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        doReturn(LabelFormat.PDF_A4).when(labelConfigurationServicePort).currentLabelFormat();
        doReturn(new Shipment(shipmentId, null, LabelParty.unknown(), LabelParty.unknown(), null, null, null, null))
                .when(shipmentServicePort)
                .find(shipmentId);

        // when
        qrCodePort.exportShipment(response, shipmentId);

        // then
        assertThat(labelText(response)).contains("NUMER PRZESY", "MANAGER PACZKA", "—");
    }

    @Test
    void shouldExportZplLabelWhenOperatorConfiguredZpl() throws Exception {
        // given
        final ShipmentId shipmentId = new ShipmentId(1L);
        final MockHttpServletResponse response = new MockHttpServletResponse();
        doReturn(LabelFormat.ZPL).when(labelConfigurationServicePort).currentLabelFormat();
        doReturn(buildShipment()).when(shipmentServicePort).find(shipmentId);

        // when
        qrCodePort.exportShipment(response, shipmentId);

        // then
        final String zpl = response.getContentAsString(StandardCharsets.UTF_8);
        assertThat(response.getHeader("Content-Disposition")).endsWith(".zpl");
        assertThat(zpl).startsWith("^XA").endsWith("^XZ\n")
                .contains("^FD30-000 KRAKÓW^FS", "^FDA. N.^FS", "^FD2.5 kg^FS", "^BCN", "^FDINP000000001^FS");
    }

    @Test
    void shouldMaskPartyToInitials() {
        // when
        final LabelParty party = LabelParty.of(" jan ", "Kowalski", "Katowice", "40-000");

        // then
        assertThat(party.initials()).isEqualTo("J. K.");
        assertThat(LabelParty.of(null, " ", "Katowice", "40-000").initials()).isNull();
    }

    private Shipment buildShipment() {
        return new Shipment(
                new ShipmentId(1L),
                new TrackingNumber("INP000000001"),
                LabelParty.of("Jan", "Kowalski", "Katowice", "40-000"),
                LabelParty.of("Anna", "Nowak", "Kraków", "30-000"),
                new BigDecimal("2.500"),
                new ParcelDimensions(new BigDecimal("30"), new BigDecimal("20.0"), new BigDecimal("10"), "CM"),
                ShipmentPriority.HIGH,
                "EXPRESS");
    }

    private String labelText(final MockHttpServletResponse response) throws Exception {
        final PdfReader reader = new PdfReader(response.getContentAsByteArray());
        try {
            return new PdfTextExtractor(reader).getTextFromPage(1).replaceAll("\\s+", " ");
        } finally {
            reader.close();
        }
    }
}
