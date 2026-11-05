package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.document;

import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.ShipmentId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentStatus;
import com.warehouse.deliverynetwork.domain.enumeration.DepartmentType;
import com.warehouse.deliverynetwork.domain.service.ShipmentHandlingPlanFactory;
import com.warehouse.deliverynetwork.domain.vo.DepartmentAddress;
import com.warehouse.deliverynetwork.domain.vo.DepartmentLocation;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentData;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentFile;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PdfShipmentRouteDocumentServiceAdapterTest {

    private final PdfShipmentRouteDocumentServiceAdapter renderer =
            new PdfShipmentRouteDocumentServiceAdapter(ZoneId.of("Europe/Warsaw"));

    @ParameterizedTest
    @ValueSource(strings = {"pl", "en", "de"})
    void shouldRenderRouteDocumentAsPdf(final String language) {
        final ShipmentRouteDocumentFile document =
                this.renderer.renderRouteDocument(data(routeDepartments(), Locale.of(language)));

        assertEquals("%PDF-", header(document.content()));
        assertTrue(document.content().length > 1000);
    }

    @ParameterizedTest
    @ValueSource(strings = {"pl", "en", "de"})
    void shouldRenderHandlingPlanAsPdf(final String language) {
        final ShipmentRouteDocumentFile document =
                this.renderer.renderHandlingPlan(data(routeDepartments(), Locale.of(language)));

        assertEquals("%PDF-", header(document.content()));
        assertTrue(document.content().length > 1000);
    }

    @Test
    void shouldNameDocumentsInRequestedLanguage() {
        final ShipmentRouteDocumentData data = data(routeDepartments(), Locale.of("de"));

        assertEquals("sendungsroute-TN-___-1.pdf", this.renderer.renderRouteDocument(data).fileName());
        assertEquals("umschlagplan-TN-___-1.pdf", this.renderer.renderHandlingPlan(data).fileName());
    }

    @Test
    void shouldUsePolishTextsForUnsupportedLanguage() {
        final ShipmentRouteDocumentData data = data(routeDepartments(), Locale.of("fr"));

        assertEquals("trasa-przesylki-TN-___-1.pdf", this.renderer.renderRouteDocument(data).fileName());
    }

    @Test
    void shouldRenderDocumentsWhenOriginDeliversShipment() {
        final ShipmentRouteDocumentData data = data(List.of(routeDepartments().getFirst()), Locale.of("en"));

        assertEquals("%PDF-", header(this.renderer.renderRouteDocument(data).content()));
        assertEquals("%PDF-", header(this.renderer.renderHandlingPlan(data).content()));
    }

    private static List<DepartmentNode> routeDepartments() {
        return List.of(
                new DepartmentNode(new DepartmentId(1L), new DepartmentCode("KT1"), DepartmentType.BRANCH,
                        DepartmentStatus.ACTIVE, new DepartmentLocation(50.29, 18.67)),
                new DepartmentNode(new DepartmentId(2L), new DepartmentCode("NCS"), DepartmentType.SORTING_FACILITY,
                        DepartmentStatus.ACTIVE, new DepartmentLocation(51.76, 19.46)),
                new DepartmentNode(new DepartmentId(3L), new DepartmentCode("GDP"), DepartmentType.BRANCH,
                        DepartmentStatus.ACTIVE, null));
    }

    private static ShipmentRouteDocumentData data(final List<DepartmentNode> departments, final Locale locale) {
        return new ShipmentRouteDocumentData(
                new ShipmentId(100L),
                new TrackingNumber("TN-ŁÓDŹ-1"),
                departments,
                Map.of(new DepartmentId(1L), new DepartmentAddress("Zwycięstwa 1", "44-100", "Gliwice", "PL")),
                new ShipmentHandlingPlanFactory().create(departments),
                null,
                Instant.parse("2026-10-29T10:00:00Z"),
                locale);
    }

    private static String header(final byte[] document) {
        return new String(Arrays.copyOf(document, 5), StandardCharsets.US_ASCII);
    }
}
