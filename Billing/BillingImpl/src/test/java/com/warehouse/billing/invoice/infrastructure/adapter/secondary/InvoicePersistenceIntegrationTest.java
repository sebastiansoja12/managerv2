package com.warehouse.billing.invoice.infrastructure.adapter.secondary;

import com.warehouse.billing.invoice.application.port.primary.command.FindInvoicesCommand;
import com.warehouse.billing.invoice.application.port.primary.result.InvoicePage;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceNumberSequenceRepository;
import com.warehouse.billing.invoice.application.port.secondary.InvoiceRepository;
import com.warehouse.billing.invoice.domain.enumeration.InvoiceType;
import com.warehouse.billing.invoice.domain.model.Invoice;
import com.warehouse.billing.invoice.domain.model.InvoiceFixture;
import com.warehouse.commonassets.identificator.OperatorId;
import com.warehouse.commonassets.identificator.PaymentId;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionTemplate;

import java.time.YearMonth;

import static com.warehouse.billing.invoice.domain.model.InvoiceFixture.DOCUMENT_ID;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@ExtendWith(SpringExtension.class)
@DataJpaTest(properties = {
        "spring.jpa.hibernate.ddl-auto=create-drop",
        "spring.liquibase.enabled=false"
})
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.ANY)
@ContextConfiguration(classes = InvoicePersistenceTestConfiguration.class)
@Transactional(propagation = Propagation.NOT_SUPPORTED)
class InvoicePersistenceIntegrationTest {

    private static final YearMonth NOVEMBER = YearMonth.of(2026, 11);

    @Autowired
    private InvoiceRepository invoiceRepository;

    @Autowired
    private InvoiceNumberSequenceRepository invoiceNumberSequenceRepository;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @AfterEach
    void resetOperator() {
        InvoicePersistenceTestConfiguration.CURRENT_OPERATOR.set(InvoicePersistenceTestConfiguration.OPERATOR_ID);
    }

    @Test
    void shouldStoreInvoiceWithLinesAndFindItByPayment() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        final Invoice invoice = InvoiceFixture.invoice(new PaymentId(31L), 1);
        invoice.attachDocument(DOCUMENT_ID);
        transaction.executeWithoutResult(status -> this.invoiceRepository.save(invoice));

        final Invoice stored = transaction.execute(status ->
                this.invoiceRepository.findByPayment(new PaymentId(31L), InvoiceType.INVOICE).orElseThrow());
        final InvoicePage page = transaction.execute(status ->
                this.invoiceRepository.find(new FindInvoicesCommand(InvoiceFixture.PRICE_QUOTE_ID, 0, 10)));

        assertThat(stored.number()).isEqualTo("FV/2026/11/0001");
        assertThat(stored.lines()).hasSize(1);
        assertThat(stored.gross().getAmount()).isEqualByComparingTo("18.45");
        assertThat(stored.buyer()).isEqualTo(invoice.buyer());
        assertThat(stored.seller()).isEqualTo(invoice.seller());
        assertThat(stored.documentId()).contains(DOCUMENT_ID);
        assertThat(this.invoiceRepository.findById(invoice.id())).isPresent();
        assertThat(page.invoices()).extracting(Invoice::id).contains(invoice.id());
    }

    @Test
    void shouldRejectSecondInvoiceOfSameTypeForPayment() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);
        transaction.executeWithoutResult(status ->
                this.invoiceRepository.save(InvoiceFixture.invoice(new PaymentId(32L), 2)));

        assertThatThrownBy(() -> transaction.executeWithoutResult(status ->
                this.invoiceRepository.save(InvoiceFixture.invoice(new PaymentId(32L), 3))))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void shouldIncrementSequencePerOperatorAndMonth() {
        final TransactionTemplate transaction = new TransactionTemplate(this.transactionManager);

        final Long first = transaction.execute(status ->
                this.invoiceNumberSequenceRepository.next(InvoiceType.INVOICE, NOVEMBER));
        final Long second = transaction.execute(status ->
                this.invoiceNumberSequenceRepository.next(InvoiceType.INVOICE, NOVEMBER));
        final Long correction = transaction.execute(status ->
                this.invoiceNumberSequenceRepository.next(InvoiceType.CORRECTION, NOVEMBER));
        final Long december = transaction.execute(status ->
                this.invoiceNumberSequenceRepository.next(InvoiceType.INVOICE, NOVEMBER.plusMonths(1)));
        InvoicePersistenceTestConfiguration.CURRENT_OPERATOR.set(OperatorId.of(11L));
        final Long otherOperator = transaction.execute(status ->
                this.invoiceNumberSequenceRepository.next(InvoiceType.INVOICE, NOVEMBER));

        assertThat(first).isEqualTo(1L);
        assertThat(second).isEqualTo(2L);
        assertThat(correction).isEqualTo(1L);
        assertThat(december).isEqualTo(1L);
        assertThat(otherOperator).isEqualTo(1L);
    }
}
