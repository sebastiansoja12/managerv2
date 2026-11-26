package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.parser.PdfTextExtractor;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.model.InvoiceFixture;
import com.warehouse.billing.invoice.domain.vo.InvoiceDocument;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.document.api.DocumentFile;
import com.warehouse.document.api.DocumentMetadata;
import com.warehouse.document.api.DocumentStorageApiService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.time.Clock;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.Optional;
import java.util.UUID;

import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.PAID_AT;
import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.USER_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PdfInvoiceDocumentServiceAdapterTest {

    private final DocumentStorageApiService documentStorageApiService = mock(DocumentStorageApiService.class);

    private final PdfInvoiceDocumentServiceAdapter adapter = new PdfInvoiceDocumentServiceAdapter(
            this.documentStorageApiService, ZoneId.of("Europe/Warsaw"), Clock.fixed(PAID_AT, ZoneOffset.UTC));

    @Test
    void shouldStoreInvoicePdfWithPolishCharacters() throws IOException {
        final Invoice invoice = InvoiceFixture.invoice(new PaymentId(1L), 1);

        final UUID documentId = this.adapter.store(invoice, USER_ID);

        final ArgumentCaptor<DocumentFile> file = ArgumentCaptor.forClass(DocumentFile.class);
        verify(this.documentStorageApiService).save(file.capture());
        final DocumentMetadata metadata = file.getValue().metadata();
        assertThat(metadata.documentId()).isEqualTo(documentId);
        assertThat(metadata.filename()).isEqualTo("FV_2026_11_0001.pdf");
        assertThat(metadata.contentType()).isEqualTo("application/pdf");
        assertThat(metadata.uploadedByUserId()).isEqualTo(USER_ID);
        assertThat(metadata.sizeBytes()).isEqualTo(file.getValue().content().length);
        final String text = new PdfTextExtractor(new PdfReader(file.getValue().content())).getTextFromPage(1);
        assertThat(text).contains("FV/2026/11/0001", "Żółkiewskiego", "Kraków", "Przesyłka", "zapłaty",
                "18,45");
    }

    @Test
    void shouldLoadStoredInvoicePdf() {
        final UUID documentId = UUID.randomUUID();
        final byte[] content = {1, 2, 3};
        when(this.documentStorageApiService.findFile(documentId)).thenReturn(Optional.of(new DocumentFile(
                new DocumentMetadata(documentId, "FV_2026_11_0001.pdf", "application/pdf", 3, null, PAID_AT),
                content)));

        final Optional<InvoiceDocument> document = this.adapter.load(documentId);

        assertThat(document).hasValueSatisfying(pdf -> {
            assertThat(pdf.fileName()).isEqualTo("FV_2026_11_0001.pdf");
            assertThat(pdf.content()).isEqualTo(content);
        });
    }
}
