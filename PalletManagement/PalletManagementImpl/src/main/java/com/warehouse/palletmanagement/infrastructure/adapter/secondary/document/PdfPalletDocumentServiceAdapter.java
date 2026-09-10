package com.warehouse.palletmanagement.infrastructure.adapter.secondary.document;

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
import com.warehouse.palletmanagement.application.port.secondary.PalletDocumentRendererServicePort;
import com.warehouse.palletmanagement.domain.enumeration.PalletStatus;
import com.warehouse.palletmanagement.domain.enumeration.TransportType;
import com.warehouse.palletmanagement.domain.model.PalletDocumentData;
import com.warehouse.palletmanagement.domain.vo.PalletCarrier;
import com.warehouse.palletmanagement.domain.vo.PalletDepartment;
import com.warehouse.palletmanagement.domain.vo.PalletDetails;
import com.warehouse.palletmanagement.domain.vo.PalletDocumentShipment;
import com.warehouse.palletmanagement.domain.vo.PalletWeight;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PdfPalletDocumentServiceAdapter implements PalletDocumentRendererServicePort {

    private static final String DASH = "—";
    private static final Color MUTED = new Color(90, 90, 90);
    private static final Color HEADER_BACKGROUND = new Color(235, 235, 235);
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    private static final Map<PalletStatus, String> STATUS_LABELS = new EnumMap<>(Map.of(
            PalletStatus.CREATED, "Utworzona",
            PalletStatus.PREPARING, "W przygotowaniu",
            PalletStatus.NOT_READY, "Niegotowa",
            PalletStatus.READY, "Gotowa do załadunku",
            PalletStatus.LOADED, "Załadowana",
            PalletStatus.IN_TRANSIT, "W transporcie",
            PalletStatus.DELIVERED, "Dostarczona",
            PalletStatus.CANCELLED, "Anulowana"));

    private static final Map<TransportType, String> TRANSPORT_LABELS = new EnumMap<>(Map.of(
            TransportType.OWN_FLEET, "Flota własna",
            TransportType.EXTERNAL_CARRIER, "Przewoźnik zewnętrzny",
            TransportType.COURIER_SERVICE, "Firma kurierska"));

    private final ZoneId zoneId;
    private final BaseFont baseFont;
    private final BaseFont boldFont;

    public PdfPalletDocumentServiceAdapter(final ZoneId zoneId) {
        this.zoneId = zoneId == null ? ZoneId.systemDefault() : zoneId;
        this.baseFont = font(BaseFont.HELVETICA);
        this.boldFont = font(BaseFont.HELVETICA_BOLD);
    }

    @Override
    public byte[] renderLabel(final PalletDocumentData data) {
        final PalletDetails pallet = data.pallet();
        final String trackingNumber = pallet.trackingNumber().value();
        return render(PageSize.A6, 14, (document) -> {
            final PdfPTable header = new PdfPTable(new float[]{3f, 2f});
            header.setWidthPercentage(100);
            final PdfPCell title = borderless();
            title.addElement(new Paragraph("PALETA", font(boldFont, 16)));
            title.addElement(new Paragraph(statusLabel(pallet.palletStatus()), font(baseFont, 8, MUTED)));
            header.addCell(title);
            final PdfPCell qr = borderless();
            qr.setHorizontalAlignment(Element.ALIGN_RIGHT);
            qr.addElement(image(qrCode(trackingNumber, 240), 68, 68, Element.ALIGN_RIGHT));
            header.addCell(qr);
            document.add(header);

            document.add(image(code128(trackingNumber), 270, 52, Element.ALIGN_CENTER));
            final Paragraph number = new Paragraph(trackingNumber, font(boldFont, 15));
            number.setAlignment(Element.ALIGN_CENTER);
            number.setSpacingAfter(6);
            document.add(number);

            final PdfPTable route = new PdfPTable(2);
            route.setWidthPercentage(100);
            route.addCell(routeCell("NADANIE", data.origin()));
            route.addCell(routeCell("DORĘCZENIE", data.destination()));
            document.add(route);

            final PdfPTable facts = new PdfPTable(2);
            facts.setWidthPercentage(100);
            facts.setSpacingBefore(6);
            fact(facts, "Przesyłki", String.valueOf(pallet.shipmentIds().size()));
            fact(facts, "Waga brutto", weight(pallet.grossWeight()));
            fact(facts, "Maks. waga", weight(pallet.maxGrossWeight()));
            fact(facts, "Plomba", text(pallet.sealNumber()));
            final PalletCarrier carrier = pallet.carrier();
            fact(facts, "Przewoźnik", carrier == null ? DASH : carrier.carrierName());
            fact(facts, "Pojazd", carrier == null ? DASH : vehicle(carrier));
            document.add(facts);

            final Paragraph footer = new Paragraph("Utworzona " + dateTime(pallet.created())
                    + " · wydruk " + dateTime(data.generatedAt()), font(baseFont, 6, MUTED));
            footer.setSpacingBefore(6);
            document.add(footer);
        });
    }

    @Override
    public byte[] renderTransportDocument(final PalletDocumentData data) {
        final PalletDetails pallet = data.pallet();
        final String trackingNumber = pallet.trackingNumber().value();
        return render(PageSize.A4, 36, (document) -> {
            final PdfPTable header = new PdfPTable(new float[]{4f, 1f});
            header.setWidthPercentage(100);
            final PdfPCell title = borderless();
            title.addElement(new Paragraph("List przewozowy palety", font(boldFont, 18)));
            title.addElement(new Paragraph("Nr dokumentu: " + data.documentNumber(), font(boldFont, 10)));
            title.addElement(new Paragraph("Numer palety: " + trackingNumber + " · status: "
                    + statusLabel(pallet.palletStatus()), font(baseFont, 9)));
            title.addElement(new Paragraph("Wygenerowano: " + dateTime(data.generatedAt()), font(baseFont, 8, MUTED)));
            header.addCell(title);
            final PdfPCell qr = borderless();
            qr.addElement(image(qrCode(trackingNumber, 300), 80, 80, Element.ALIGN_RIGHT));
            header.addCell(qr);
            document.add(header);
            document.add(image(code128(trackingNumber), 260, 40, Element.ALIGN_LEFT));

            section(document, "1. Trasa");
            final PdfPTable route = new PdfPTable(2);
            route.setWidthPercentage(100);
            route.addCell(partyCell("Oddział nadający (nadawca)", data.origin()));
            route.addCell(partyCell("Oddział doręczający (odbiorca)", data.destination()));
            document.add(route);

            section(document, "2. Przewoźnik i środek transportu");
            final PdfPTable carrierTable = new PdfPTable(new float[]{1.3f, 2f, 1.3f, 2f});
            carrierTable.setWidthPercentage(100);
            final PalletCarrier carrier = pallet.carrier();
            labelled(carrierTable, "Przewoźnik", carrier == null ? "Nie przypisano" : carrier.carrierName());
            labelled(carrierTable, "Rodzaj transportu", carrier == null ? DASH : transportLabel(carrier.transportType()));
            labelled(carrierTable, "Kierowca", carrier == null ? DASH : text(carrier.driverName()));
            labelled(carrierTable, "Telefon", carrier == null ? DASH : text(carrier.driverPhone()));
            labelled(carrierTable, "Nr rej. pojazdu", carrier == null ? DASH : text(carrier.vehicleRegistrationNumber()));
            labelled(carrierTable, "Nr rej. naczepy", carrier == null ? DASH : text(carrier.trailerRegistrationNumber()));
            labelled(carrierTable, "Numer plomby", text(pallet.sealNumber()));
            labelled(carrierTable, "Przypisano", carrier == null ? DASH : dateTime(carrier.assignedAt()));
            document.add(carrierTable);

            section(document, "3. Ładunek");
            final PdfPTable cargo = new PdfPTable(new float[]{1.3f, 2f, 1.3f, 2f});
            cargo.setWidthPercentage(100);
            labelled(cargo, "Liczba przesyłek", String.valueOf(pallet.shipmentIds().size()));
            labelled(cargo, "Waga ładunku", kilograms(data.cargoWeightInKilograms()));
            labelled(cargo, "Waga brutto palety", weight(pallet.grossWeight()));
            labelled(cargo, "Maks. waga brutto", weight(pallet.maxGrossWeight()));
            document.add(cargo);

            final PdfPTable shipments = new PdfPTable(new float[]{0.5f, 2.6f, 1.6f, 2f, 1f, 1f});
            shipments.setWidthPercentage(100);
            shipments.setSpacingBefore(8);
            shipments.setHeaderRows(1);
            Stream.of("Lp.", "Numer przesyłki", "Status", "Miasto odbiorcy", "Kod pocztowy", "Waga [kg]")
                    .forEach(column -> shipments.addCell(headerCell(column)));
            final List<PalletDocumentShipment> lines = data.shipments();
            if (lines.isEmpty()) {
                final PdfPCell empty = cell("Paleta nie zawiera przesyłek", baseFont, 8);
                empty.setColspan(6);
                shipments.addCell(empty);
            }
            for (int index = 0; index < lines.size(); index++) {
                final PdfPDocumentLine line = new PdfPDocumentLine(lines.get(index));
                shipments.addCell(cell(String.valueOf(index + 1), baseFont, 8));
                shipments.addCell(cell(line.trackingNumber(), boldFont, 8));
                shipments.addCell(cell(line.status(), baseFont, 8));
                shipments.addCell(cell(text(line.shipment().recipientCity()), baseFont, 8));
                shipments.addCell(cell(text(line.shipment().recipientPostalCode()), baseFont, 8));
                shipments.addCell(numberCell(line.weight()));
            }
            final PdfPCell totalLabel = cell("Razem", boldFont, 8);
            totalLabel.setColspan(5);
            totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
            shipments.addCell(totalLabel);
            final PdfPCell total = numberCell(plain(data.cargoWeightInKilograms()));
            total.setPhrase(new Phrase(plain(data.cargoWeightInKilograms()), font(boldFont, 8)));
            shipments.addCell(total);
            document.add(shipments);

            section(document, "4. Potwierdzenia");
            final PdfPTable signatures = new PdfPTable(3);
            signatures.setWidthPercentage(100);
            Stream.of("Wydał (oddział nadający)", "Przewoźnik / kierowca", "Odebrał (oddział doręczający)")
                    .forEach(label -> signatures.addCell(signatureCell(label)));
            document.add(signatures);
        });
    }

    private byte[] render(final Rectangle pageSize, final float margin, final DocumentWriter writer) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final Document document = new Document(pageSize, margin, margin, margin, margin);
        try {
            PdfWriter.getInstance(document, output);
            document.open();
            writer.write(document);
        } catch (final DocumentException exception) {
            throw new IllegalStateException("Pallet document could not be rendered", exception);
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

    private PdfPCell routeCell(final String label, final PalletDepartment department) {
        final PdfPCell cell = new PdfPCell();
        cell.setPadding(4);
        cell.addElement(new Paragraph(label, font(baseFont, 6, MUTED)));
        cell.addElement(new Paragraph(department == null ? DASH : text(department.departmentCode()), font(boldFont, 14)));
        cell.addElement(new Paragraph(department == null ? "" : text(department.city()), font(baseFont, 8)));
        return cell;
    }

    private PdfPCell partyCell(final String label, final PalletDepartment department) {
        final PdfPCell cell = new PdfPCell();
        cell.setPadding(6);
        cell.addElement(new Paragraph(label, font(baseFont, 7, MUTED)));
        if (department == null) {
            cell.addElement(new Paragraph("Nie wskazano", font(boldFont, 10)));
            return cell;
        }
        cell.addElement(new Paragraph(text(department.departmentCode()), font(boldFont, 12)));
        cell.addElement(new Paragraph(join(department.street()), font(baseFont, 9)));
        cell.addElement(new Paragraph(join(department.zipCode(), department.city(), department.countryCode()),
                font(baseFont, 9)));
        return cell;
    }

    private PdfPCell signatureCell(final String label) {
        final PdfPCell cell = new PdfPCell();
        cell.setFixedHeight(70);
        cell.setPadding(6);
        cell.addElement(new Paragraph(label, font(baseFont, 7, MUTED)));
        final Paragraph line = new Paragraph("\n\n\ndata, podpis", font(baseFont, 6, MUTED));
        line.setAlignment(Element.ALIGN_CENTER);
        cell.addElement(line);
        return cell;
    }

    private void fact(final PdfPTable table, final String label, final String value) {
        final PdfPCell cell = new PdfPCell();
        cell.setPadding(3);
        cell.addElement(new Paragraph(label, font(baseFont, 6, MUTED)));
        cell.addElement(new Paragraph(value, font(boldFont, 8)));
        table.addCell(cell);
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

    private PdfPCell cell(final String text, final BaseFont font, final float size) {
        final PdfPCell cell = new PdfPCell(new Phrase(text == null ? DASH : text, font(font, size)));
        cell.setPadding(4);
        return cell;
    }

    private static PdfPCell borderless() {
        final PdfPCell cell = new PdfPCell();
        cell.setBorder(Rectangle.NO_BORDER);
        cell.setPadding(0);
        return cell;
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

    static byte[] code128(final String value) {
        return encode(new Code128Writer(), value, BarcodeFormat.CODE_128, 600, 120, 0);
    }

    static byte[] qrCode(final String value, final int size) {
        return encode(new QRCodeWriter(), value, BarcodeFormat.QR_CODE, size, size, 1);
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

    private String dateTime(final Instant instant) {
        return instant == null ? DASH : DATE_TIME.format(instant.atZone(this.zoneId));
    }

    private static String statusLabel(final PalletStatus status) {
        return status == null ? DASH : STATUS_LABELS.getOrDefault(status, status.name());
    }

    private static String transportLabel(final TransportType transportType) {
        return transportType == null ? DASH : TRANSPORT_LABELS.getOrDefault(transportType, transportType.name());
    }

    private static String vehicle(final PalletCarrier carrier) {
        return carrier.trailerRegistrationNumber() == null
                ? text(carrier.vehicleRegistrationNumber())
                : carrier.vehicleRegistrationNumber() + " / " + carrier.trailerRegistrationNumber();
    }

    private static String weight(final PalletWeight weight) {
        return weight == null || weight.value() == null
                ? DASH
                : plain(weight.value()) + " " + (weight.unit() == null ? "KG" : weight.unit());
    }

    private static String kilograms(final BigDecimal value) {
        return value == null ? DASH : plain(value) + " KG";
    }

    private static String plain(final BigDecimal value) {
        return value == null ? DASH : value.setScale(3, RoundingMode.HALF_UP).stripTrailingZeros().toPlainString();
    }

    private static String text(final String value) {
        return value == null || value.isBlank() ? DASH : value;
    }

    private static String join(final String... values) {
        final String joined = Stream.of(values)
                .filter(Objects::nonNull)
                .filter(value -> !value.isBlank())
                .collect(Collectors.joining(" "));
        return joined.isBlank() ? DASH : joined;
    }

    private record PdfPDocumentLine(PalletDocumentShipment shipment) {

        String trackingNumber() {
            return shipment.trackingNumber() == null
                    ? "ID " + shipment.shipmentId().getValue()
                    : shipment.trackingNumber();
        }

        String status() {
            return shipment.status() == null ? DASH : shipment.status().name();
        }

        String weight() {
            return plain(shipment.weightInKilograms());
        }
    }

    @FunctionalInterface
    private interface DocumentWriter {
        void write(Document document) throws DocumentException;
    }
}
