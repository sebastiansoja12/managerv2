package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Element;
import com.lowagie.text.Font;
import com.lowagie.text.PageSize;
import com.lowagie.text.Paragraph;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceDocumentServicePort;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.vo.InvoiceDocument;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.Money;
import com.warehouse.document.api.DocumentFile;
import com.warehouse.document.api.DocumentMetadata;
import com.warehouse.document.api.DocumentStorageApiService;

import java.awt.Color;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.EnumMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PdfInvoiceDocumentServiceAdapter implements InvoiceDocumentServicePort {

    private static final String CONTENT_TYPE = "application/pdf";
    private static final String DASH = "—";
    private static final Color MUTED = new Color(90, 90, 90);
    private static final Color HEADER_BACKGROUND = new Color(235, 235, 235);
    private static final DateTimeFormatter DATE = DateTimeFormatter.ofPattern("dd.MM.yyyy");
    private static final DateTimeFormatter DATE_TIME = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    private static final Locale POLISH = Locale.forLanguageTag("pl-PL");

    private static final Map<PaymentMethod, String> PAYMENT_LABELS = new EnumMap<>(Map.of(
            PaymentMethod.CASH, "Gotówka",
            PaymentMethod.CARD, "Karta płatnicza",
            PaymentMethod.STRIPE, "Płatność online"));

    private final DocumentStorageApiService documentStorageApiService;
    private final ZoneId zoneId;
    private final Clock clock;
    private final BaseFont baseFont;
    private final BaseFont boldFont;

    public PdfInvoiceDocumentServiceAdapter(final DocumentStorageApiService documentStorageApiService,
                                            final ZoneId zoneId, final Clock clock) {
        this.documentStorageApiService = documentStorageApiService;
        this.zoneId = zoneId;
        this.clock = clock;
        this.baseFont = font(BaseFont.HELVETICA);
        this.boldFont = font(BaseFont.HELVETICA_BOLD);
    }

    @Override
    public UUID store(final Invoice invoice, final UserId uploadedBy) {
        final byte[] content = render(invoice);
        final UUID documentId = UUID.randomUUID();
        this.documentStorageApiService.save(new DocumentFile(new DocumentMetadata(documentId,
                invoice.number().replace('/', '_') + ".pdf", CONTENT_TYPE, content.length, uploadedBy,
                this.clock.instant()), content));
        return documentId;
    }

    @Override
    public Optional<InvoiceDocument> load(final UUID documentId) {
        return this.documentStorageApiService.findFile(documentId)
                .map(file -> new InvoiceDocument(file.metadata().filename(), file.content()));
    }

    private byte[] render(final Invoice invoice) {
        final ByteArrayOutputStream output = new ByteArrayOutputStream();
        final Document document = new Document(PageSize.A4, 36, 36, 36, 36);
        try {
            PdfWriter.getInstance(document, output);
            document.open();
            write(document, invoice);
        } catch (final DocumentException exception) {
            throw new IllegalStateException("Invoice document could not be rendered", exception);
        } finally {
            if (document.isOpen()) {
                document.close();
            }
        }
        return output.toByteArray();
    }

    private void write(final Document document, final Invoice invoice) throws DocumentException {
        final boolean correction = InvoiceType.CORRECTION.equals(invoice.type());
        document.add(new Paragraph(correction ? "Faktura korygująca" : "Faktura VAT", font(boldFont, 18)));
        document.add(new Paragraph("Nr " + invoice.number(), font(boldFont, 12)));
        if (correction) {
            document.add(new Paragraph("Dotyczy faktury: " + text(invoice.correctedInvoiceNumber()),
                    font(baseFont, 9)));
        }
        document.add(new Paragraph("Data wystawienia: " + date(invoice.issueDate())
                + " · data sprzedaży: " + date(invoice.saleDate()), font(baseFont, 9)));

        final PdfPTable parties = new PdfPTable(2);
        parties.setWidthPercentage(100);
        parties.setSpacingBefore(12);
        parties.addCell(partyCell("Sprzedawca", invoice.seller()));
        parties.addCell(partyCell("Nabywca", invoice.buyer()));
        document.add(parties);

        final PdfPTable lines = new PdfPTable(new float[]{0.5f, 4f, 1.3f, 0.8f, 1.2f, 1.3f});
        lines.setWidthPercentage(100);
        lines.setSpacingBefore(12);
        lines.setHeaderRows(1);
        Stream.of("Lp.", "Nazwa usługi", "Netto", "VAT %", "VAT", "Brutto")
                .forEach(column -> lines.addCell(headerCell(column)));
        final List<InvoiceLine> items = invoice.lines();
        for (int index = 0; index < items.size(); index++) {
            final InvoiceLine line = items.get(index);
            lines.addCell(cell(String.valueOf(index + 1), baseFont));
            lines.addCell(cell(line.description(), baseFont));
            lines.addCell(amountCell(amount(line.net()), baseFont));
            lines.addCell(amountCell(percent(line.vatPercent()), baseFont));
            lines.addCell(amountCell(amount(line.vat()), baseFont));
            lines.addCell(amountCell(amount(line.gross()), baseFont));
        }
        final PdfPCell totalLabel = cell("Razem", boldFont);
        totalLabel.setColspan(2);
        totalLabel.setHorizontalAlignment(Element.ALIGN_RIGHT);
        lines.addCell(totalLabel);
        lines.addCell(amountCell(amount(invoice.net()), boldFont));
        lines.addCell(amountCell("", boldFont));
        lines.addCell(amountCell(amount(invoice.vat()), boldFont));
        lines.addCell(amountCell(amount(invoice.gross()), boldFont));
        document.add(lines);

        final Paragraph total = new Paragraph((correction ? "Do zwrotu: " : "Razem do zapłaty: ")
                + amount(correction ? invoice.gross().negate() : invoice.gross()) + " " + invoice.currency(),
                font(boldFont, 12));
        total.setSpacingBefore(12);
        document.add(total);
        document.add(new Paragraph("Sposób płatności: " + paymentLabel(invoice.paymentMethod())
                + (correction ? "" : " · zapłacono"), font(baseFont, 9)));
        document.add(new Paragraph("Rachunek bankowy: " + text(invoice.sellerBankAccount()), font(baseFont, 9)));

        final Paragraph footer = new Paragraph("Wygenerowano " + dateTime(invoice.issuedAt()),
                font(baseFont, 7, MUTED));
        footer.setSpacingBefore(18);
        document.add(footer);
    }

    private PdfPCell partyCell(final String label, final InvoiceParty party) {
        final PdfPCell cell = new PdfPCell();
        cell.setPadding(6);
        cell.addElement(new Paragraph(label, font(baseFont, 7, MUTED)));
        cell.addElement(new Paragraph(text(party.name()), font(boldFont, 10)));
        cell.addElement(new Paragraph(join(party.street()), font(baseFont, 9)));
        cell.addElement(new Paragraph(join(party.postalCode(), party.city(), party.countryCode()),
                font(baseFont, 9)));
        cell.addElement(new Paragraph("NIP: " + (party.taxId() == null ? DASH : text(party.taxId().value())),
                font(baseFont, 9)));
        return cell;
    }

    private PdfPCell headerCell(final String text) {
        final PdfPCell cell = cell(text, boldFont);
        cell.setBackgroundColor(HEADER_BACKGROUND);
        return cell;
    }

    private PdfPCell amountCell(final String text, final BaseFont font) {
        final PdfPCell cell = cell(text, font);
        cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        return cell;
    }

    private PdfPCell cell(final String text, final BaseFont font) {
        final PdfPCell cell = new PdfPCell(new Phrase(text, font(font, 8)));
        cell.setPadding(4);
        cell.setBorder(Rectangle.BOX);
        return cell;
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

    private static String amount(final Money money) {
        return new DecimalFormat("#,##0.00", DecimalFormatSymbols.getInstance(POLISH)).format(money.getAmount());
    }

    private static String percent(final BigDecimal percent) {
        return percent.stripTrailingZeros().toPlainString() + "%";
    }

    private static String date(final LocalDate date) {
        return date == null ? DASH : DATE.format(date);
    }

    private String dateTime(final Instant instant) {
        return instant == null ? DASH : DATE_TIME.format(instant.atZone(this.zoneId));
    }

    private static String paymentLabel(final PaymentMethod method) {
        return method == null ? DASH : PAYMENT_LABELS.getOrDefault(method, method.name());
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
}
