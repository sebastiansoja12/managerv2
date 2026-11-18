package com.warehouse.deliverynetwork.infrastructure.adapter.secondary.document;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.warehouse.deliverynetwork.application.port.secondary.ShipmentRouteDocumentRendererServicePort;
import com.warehouse.deliverynetwork.domain.vo.DepartmentAddress;
import com.warehouse.deliverynetwork.domain.vo.DepartmentNode;
import com.warehouse.deliverynetwork.domain.vo.HandlingOperation;
import com.warehouse.deliverynetwork.domain.vo.RouteLeg;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentData;
import com.warehouse.deliverynetwork.domain.vo.ShipmentRouteDocumentFile;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.text.MessageFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PdfShipmentRouteDocumentServiceAdapter implements ShipmentRouteDocumentRendererServicePort {

    private static final String DASH = "—";
    private static final Color MUTED = new Color(90, 90, 90);
    private static final Color HEADER_BACKGROUND = new Color(235, 235, 235);
    private static final Color DEPARTMENT_BACKGROUND = new Color(245, 247, 250);
    private static final Color DESTINATION_BACKGROUND = new Color(225, 245, 236);
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static final String MESSAGES = "i18n.shipment_route_document";

    private final ZoneId zoneId;
    private final BaseFont baseFont;
    private final BaseFont boldFont;

    public PdfShipmentRouteDocumentServiceAdapter(final ZoneId zoneId) {
        this.zoneId = zoneId;
        this.baseFont = font(BaseFont.HELVETICA);
        this.boldFont = font(BaseFont.HELVETICA_BOLD);
    }

    @Override
    public ShipmentRouteDocumentFile renderRouteDocument(final ShipmentRouteDocumentData data) {
        final Texts texts = texts(data);
        return file(texts.text("file.route"), data, render(PageSize.A4, (document) -> {
            header(document, texts.text("title.route"), data, texts);

            section(document, texts.text("section.summary"));
            final PdfPTable summary = new PdfPTable(new float[]{1.4f, 2f, 1.4f, 2f});
            summary.setWidthPercentage(100);
            labelled(summary, texts.text("summary.origin"), code(origin(data)));
            labelled(summary, texts.text("summary.destination"), code(destination(data)));
            labelled(summary, texts.text("summary.legs"), String.valueOf(data.handlingPlan().legs().size()));
            labelled(summary, texts.text("summary.transfers"), String.valueOf(Math.max(data.departments().size() - 2, 0)));
            labelled(summary, texts.text("summary.routeKind"), routeKind(data, texts));
            labelled(summary, texts.text("summary.totalDistance"), kilometers(totalDistanceInKilometers(data)));
            document.add(summary);

            section(document, texts.text("section.stops"));
            final PdfPTable stops = new PdfPTable(new float[]{0.5f, 1.7f, 1.1f, 1.5f, 3.2f});
            stops.setWidthPercentage(100);
            stops.setHeaderRows(1);
            Stream.of("column.number", "column.role", "column.code", "column.type", "column.address")
                    .forEach(column -> stops.addCell(headerCell(texts.text(column))));
            final List<DepartmentNode> departments = new ArrayList<>(data.departments());
            Collections.reverse(departments);
            for (int index = 0; index < departments.size(); index++) {
                final DepartmentNode department = departments.get(index);
                final int stopNumber = departments.size() - index;
                final boolean destination = index == 0 && departments.size() > 1;
                final Color background = destination ? DESTINATION_BACKGROUND : null;
                stops.addCell(shaded(cell(String.valueOf(stopNumber), baseFont, 8), background));
                stops.addCell(shaded(cell(role(stopNumber, data.departments().size(), texts), boldFont, 8), background));
                stops.addCell(shaded(cell(code(department), boldFont, 10), background));
                stops.addCell(shaded(cell(departmentType(department, texts), baseFont, 8), background));
                stops.addCell(shaded(cell(address(data, department), baseFont, 8), background));
            }
            document.add(stops);

            section(document, texts.text("section.legs"));
            final PdfPTable legs = new PdfPTable(new float[]{0.5f, 2f, 2f, 1.2f});
            legs.setWidthPercentage(100);
            legs.setHeaderRows(1);
            Stream.of("column.number", "column.from", "column.to", "column.distance")
                    .forEach(column -> legs.addCell(headerCell(texts.text(column))));
            if (data.handlingPlan().legs().isEmpty()) {
                final PdfPCell empty = cell(texts.text("legs.empty"), baseFont, 8);
                empty.setColspan(4);
                legs.addCell(empty);
            }
            for (final RouteLeg leg : data.handlingPlan().legs()) {
                legs.addCell(cell(String.valueOf(leg.sequence()), baseFont, 8));
                legs.addCell(cell(codeWithCity(data, leg.originDepartment()), boldFont, 8));
                legs.addCell(cell(codeWithCity(data, leg.destinationDepartment()), boldFont, 8));
                legs.addCell(numberCell(kilometers(leg.distanceInKilometers())));
            }
            if (!data.handlingPlan().legs().isEmpty()) {
                final PdfPCell totalLabel = cell(texts.text("summary.totalDistance"), boldFont, 8);
                totalLabel.setColspan(3);
                legs.addCell(shaded(totalLabel, DESTINATION_BACKGROUND));
                final PdfPCell totalValue = cell(kilometers(totalDistanceInKilometers(data)), boldFont, 8);
                totalValue.setHorizontalAlignment(Element.ALIGN_RIGHT);
                legs.addCell(shaded(totalValue, DESTINATION_BACKGROUND));
            }
            document.add(legs);
        }));
    }

    @Override
    public ShipmentRouteDocumentFile renderHandlingPlan(final ShipmentRouteDocumentData data) {
        final Texts texts = texts(data);
        return file(texts.text("file.handlingPlan"), data, render(PageSize.A4, (document) -> {
            header(document, texts.text("title.handlingPlan"), data, texts);

            section(document, texts.text("section.route"));
            final Paragraph route = new Paragraph(data.departments().stream()
                    .map(PdfShipmentRouteDocumentServiceAdapter::code)
                    .collect(Collectors.joining("  →  ")), font(boldFont, 11));
            route.setSpacingAfter(2);
            document.add(route);
            document.add(new Paragraph(texts.text("plan.routeSummary", routeKind(data, texts),
                    String.valueOf(data.handlingPlan().legs().size()), kilometers(totalDistanceInKilometers(data))),
                    font(baseFont, 8, MUTED)));

            section(document, texts.text("section.operations"));
            final PdfPTable operations = new PdfPTable(new float[]{0.5f, 1.6f, 3f, 1f, 1.6f});
            operations.setWidthPercentage(100);
            operations.setHeaderRows(1);
            Stream.of("column.number", "column.operation", "column.details", "column.time", "column.signature")
                    .forEach(column -> operations.addCell(headerCell(texts.text(column))));

            DepartmentNode currentDepartment = null;
            for (final HandlingOperation operation : data.handlingPlan().operations()) {
                if (!operation.department().equals(currentDepartment)) {
                    currentDepartment = operation.department();
                    final PdfPCell departmentRow = cell(code(currentDepartment) + " · " + departmentType(currentDepartment, texts)
                            + " · " + address(data, currentDepartment), boldFont, 9);
                    departmentRow.setColspan(5);
                    departmentRow.setBackgroundColor(DEPARTMENT_BACKGROUND);
                    operations.addCell(departmentRow);
                }
                operations.addCell(cell(String.valueOf(operation.sequence()), baseFont, 8));
                operations.addCell(cell(texts.text("operation." + operation.operationType().name()), boldFont, 8));
                operations.addCell(cell(operationDetails(data, operation, texts), baseFont, 8));
                operations.addCell(signatureField());
                operations.addCell(signatureField());
            }
            document.add(operations);

            section(document, texts.text("section.confirmations"));
            final PdfPTable signatures = new PdfPTable(2);
            signatures.setWidthPercentage(100);
            signatures.addCell(signatureCell(texts.text("plan.issuedBy", code(origin(data))), texts));
            signatures.addCell(signatureCell(texts.text("plan.receivedBy", code(destination(data))), texts));
            document.add(signatures);
        }));
    }

    private void header(final Document document, final String title, final ShipmentRouteDocumentData data,
                        final Texts texts) throws DocumentException {
        final String trackingNumber = data.trackingNumber().value();
        final PdfPTable header = new PdfPTable(new float[]{4f, 1f});
        header.setWidthPercentage(100);
        final PdfPCell titleCell = borderless();
        titleCell.addElement(new Paragraph(title, font(boldFont, 18)));
        titleCell.addElement(new Paragraph(texts.text("header.trackingNumber", trackingNumber), font(boldFont, 10)));
        titleCell.addElement(new Paragraph(texts.text("header.shipmentId", String.valueOf(data.shipmentId().getValue())), font(baseFont, 8)));
        titleCell.addElement(new Paragraph(texts.text("header.generatedAt", dateTime(data.generatedAt())), font(baseFont, 8, MUTED)));
        header.addCell(titleCell);
        final PdfPCell qr = borderless();
        qr.addElement(image(qrCode(trackingNumber), 76, 76, Element.ALIGN_RIGHT));
        header.addCell(qr);
        document.add(header);
        document.add(image(code128(trackingNumber), 260, 38, Element.ALIGN_LEFT));
    }

    private String operationDetails(final ShipmentRouteDocumentData data, final HandlingOperation operation,
                                    final Texts texts) {
        final String key = "operationDetails." + operation.operationType().name();
        return operation.counterpartDepartment() == null
                ? texts.text(key)
                : texts.text(key, codeWithCity(data, operation.counterpartDepartment()));
    }

    private static Texts texts(final ShipmentRouteDocumentData data) {
        final Locale locale = data.locale() == null ? Locale.of("pl") : data.locale();
        return new Texts(ResourceBundle.getBundle(MESSAGES, locale,
                ResourceBundle.Control.getNoFallbackControl(ResourceBundle.Control.FORMAT_PROPERTIES)), locale);
    }

    private static ShipmentRouteDocumentFile file(final String name, final ShipmentRouteDocumentData data,
                                                  final byte[] content) {
        return new ShipmentRouteDocumentFile(
                name + "-" + data.trackingNumber().value().replaceAll("[^A-Za-z0-9_-]", "_") + ".pdf", content);
    }

    private byte[] render(final Rectangle pageSize, final DocumentWriter writer) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final Document document = new Document(pageSize, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(document, output);
            document.open();
            writer.write(document);
        } catch (final DocumentException exception) {
            throw new IllegalStateException("Shipment route document could not be rendered", exception);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
        return output.toByteArray();
    }

    private void section(final Document document, final String title) throws DocumentException {
        final Paragraph paragraph = new Paragraph(title, font(boldFont, 11));
        paragraph.setSpacingBefore(12);
        paragraph.setSpacingAfter(4);
        document.add(paragraph);
    }

    private void labelled(final PdfPTable table, final String label, final String value) {
        final PdfPCell labelCell = cell(label, baseFont, 8);
        labelCell.setBackgroundColor(HEADER_BACKGROUND);
        table.addCell(labelCell);
        table.addCell(cell(value, boldFont, 8));
    }

    private PdfPCell headerCell(final String text) {
        final PdfPCell cell = cell(text, boldFont, 8);
        cell.setBackgroundColor(HEADER_BACKGROUND);
        return cell;
    }

    private PdfPCell numberCell(final String text) {
        final PdfPCell cell = cell(text, baseFont, 8);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell signatureField() {
        final PdfPCell cell = cell("", baseFont, 8);
        cell.setMinimumHeight(22);
        return cell;
    }

    private PdfPCell signatureCell(final String label, final Texts texts) {
        final PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(70);
        cell.setPadding(6);
        cell.addElement(new Paragraph(label, font(baseFont, 7, MUTED)));
        final Paragraph line = new Paragraph("\n\n\n" + texts.text("signature.line"), font(baseFont, 6, MUTED));
        line.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(line);
        return cell;
    }

    private PdfPCell cell(final String text, final BaseFont font, final float size) {
        final PdfPCell cell = new PdfPCell(new Phrase(text == null ? DASH : text, font(font, size)));
        cell.setPadding(4);
        return cell;
    }

    private static PdfPCell shaded(final PdfPCell cell, final Color background) {
        if (background != null) {
            cell.setBackgroundColor(background);
        }
        return cell;
    }

    private static PdfPCell borderless() {
        final PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0);
        return cell;
    }

    private static DepartmentNode origin(final ShipmentRouteDocumentData data) {
        return data.departments().getFirst();
    }

    private static DepartmentNode destination(final ShipmentRouteDocumentData data) {
        return data.departments().getLast();
    }

    private static String role(final int stopNumber, final int stopsCount, final Texts texts) {
        if (stopsCount > 1 && stopNumber == stopsCount) {
            return texts.text("role.destination");
        }
        return stopNumber == 1 ? texts.text("role.origin") : texts.text("role.transfer");
    }

    private static String routeKind(final ShipmentRouteDocumentData data, final Texts texts) {
        final int transfers = Math.max(data.departments().size() - 2, 0);
        if (data.departments().size() == 1) {
            return texts.text("routeKind.local");
        }
        return transfers == 0
                ? texts.text("routeKind.direct")
                : texts.text("routeKind.viaTransfers", String.valueOf(transfers));
    }

    private static String code(final DepartmentNode department) {
        return department.departmentCode().getValue();
    }

    private static String departmentType(final DepartmentNode department, final Texts texts) {
        final String key = "departmentType." + department.departmentType().name();
        return texts.bundle().containsKey(key) ? texts.text(key) : department.departmentType().name();
    }

    private static String codeWithCity(final ShipmentRouteDocumentData data, final DepartmentNode department) {
        return data.address(department)
                .map(DepartmentAddress::city)
                .filter(city -> city != null && !city.isBlank())
                .map(city -> code(department) + " (" + city + ")")
                .orElse(code(department));
    }

    private static String address(final ShipmentRouteDocumentData data, final DepartmentNode department) {
        return data.address(department)
                .map(address -> join(address.street(), address.postalCode(), address.city(), address.countryCode()))
                .orElse(DASH);
    }

    private static Double totalDistanceInKilometers(final ShipmentRouteDocumentData data) {
        if (data.distanceInKilometers() != null) {
            return data.distanceInKilometers();
        }
        final List<Double> distances = data.handlingPlan().legs().stream()
                .map(RouteLeg::distanceInKilometers)
                .filter(Objects::nonNull)
                .toList();
        return distances.isEmpty() ? null : distances.stream().mapToDouble(Double::doubleValue).sum();
    }

    private static String kilometers(final Double distanceInKilometers) {
        return distanceInKilometers == null ? DASH : String.format(Locale.ROOT, "%.0f km", distanceInKilometers);
    }

    private static String join(final String... values) {
        final String joined = Stream.of(values)
                .filter(Objects::nonNull)
                .filter(value -> !value.isBlank())
                .collect(Collectors.joining(", "));
        return joined.isBlank() ? DASH : joined;
    }

    private String dateTime(final Instant instant) {
        return DATE_TIME.format(instant.atZone(this.zoneId));
    }

    private static Image image(final byte[] png, final float width, final float height, final int alignment) {
        try {
            final Image image = Image.getInstance(png);
            image.scaleToFit(width, height);
            image.setAlignment(alignment);
            return image;
        } catch (final IOException | DocumentException exception) {
            throw new IllegalStateException("Barcode image could not be created", exception);
        }
    }

    private static byte[] code128(final String value) {
        return encode(new Code128Writer(), value, BarcodeFormat.CODE_128, 600, 120, 0);
    }

    private static byte[] qrCode(final String value) {
        return encode(new QRCodeWriter(), value, BarcodeFormat.QR_CODE, 300, 300, 1);
    }

    private static byte[] encode(final Writer writer, final String value, final BarcodeFormat format,
                                 final int width, final int height, final int margin) {
        try {
            return png(writer.encode(value, format, width, height, Map.of(EncodeHintType.MARGIN, margin)));
        } catch (final WriterException exception) {
            throw new IllegalStateException("Barcode " + format + " could not be created", exception);
        }
    }

    private static byte[] png(final BitMatrix matrix) {
        try {
            final ByteArrayOutputStream output = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", output);
            return output.toByteArray();
        } catch (final IOException exception) {
            throw new UncheckedIOException(exception);
        }
    }

    private static BaseFont font(final String name) {
        try {
            return BaseFont.createFont(name, BaseFont.CP1250, BaseFont.NOT_EMBEDDED);
        } catch (final DocumentException | IOException exception) {
            throw new IllegalStateException("PDF font could not be loaded", exception);
        }
    }

    private static Font font(final BaseFont baseFont, final float size) {
        return new Font(baseFont, size);
    }

    private static Font font(final BaseFont baseFont, final float size, final Color color) {
        final Font font = new Font(baseFont, size);
        font.setColor(color);
        return font;
    }

    private record Texts(ResourceBundle bundle, Locale locale) {

        private String text(final String key, final Object... arguments) {
            final String pattern = this.bundle.getString(key);
            return arguments.length == 0 ? pattern : new MessageFormat(pattern, this.locale).format(arguments);
        }
    }

    @FunctionalInterface
    private interface DocumentWriter {
        void write(final Document document) throws DocumentException;
    }
}
