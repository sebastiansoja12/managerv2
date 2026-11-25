package com.warehouse.billing.invoice.domain.model;

import com.warehouse.billing.invoice.domain.service.InvoiceFactory;
import com.warehouse.billing.invoice.domain.service.InvoiceNumberFormatter;
import com.warehouse.billing.invoice.domain.vo.InvoiceBuyer;
import com.warehouse.billing.invoice.domain.vo.InvoiceDraft;
import com.warehouse.billing.invoice.domain.vo.InvoiceLine;
import com.warehouse.billing.invoice.domain.vo.InvoiceParty;
import com.warehouse.billing.invoice.domain.vo.InvoiceSeller;
import com.warehouse.billing.payment.domain.enumeration.PaymentChannel;
import com.warehouse.billing.payment.domain.enumeration.PaymentMethod;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.pricing.domain.enumeration.PriceQuoteLineType;
import com.warehouse.billing.pricing.domain.model.PriceQuote;
import com.warehouse.billing.pricing.domain.vo.PriceQuoteLine;
import com.warehouse.commonassets.enumeration.Currency;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceListId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.commonassets.model.Money;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.UUID;

public final class InvoiceFixture {

    public static final PaymentId PAYMENT_ID = new PaymentId(1L);

    public static final PriceQuoteId PRICE_QUOTE_ID = new PriceQuoteId(20L);

    public static final UserId USER_ID = new UserId(5L);

    public static final TaxId BUYER_TAX_ID = TaxId.of("5260250995");

    public static final Instant PAID_AT = Instant.parse("2026-11-24T12:00:00Z");

    public static final Instant REFUNDED_AT = Instant.parse("2026-12-01T10:00:00Z");

    public static final UUID DOCUMENT_ID = UUID.fromString("7f6a3a9c-1d7e-4c55-9f1e-2a3b4c5d6e7f");

    private InvoiceFixture() {
    }

    public static Payment paidAtCounter(final PaymentId paymentId, final PriceQuoteId priceQuoteId,
                                        final Instant paidAt) {
        return new Payment(paymentId, priceQuoteId, money("24.60"), PaymentChannel.COUNTER, PaymentMethod.CASH,
                PaymentStatus.PAID, null, 0, paidAt.plusSeconds(72 * 3600), paidAt.minusSeconds(3600), paidAt,
                USER_ID, new DepartmentId(100L), BUYER_TAX_ID, null, null, null, null, 1L);
    }

    public static PriceQuote quote(final PriceQuoteId priceQuoteId) {
        return new PriceQuote(priceQuoteId, new PriceListId(3L), Currency.PLN, List.of(
                new PriceQuoteLine(PriceQuoteLineType.BASE, "Przesyłka standardowa", money("15.00"),
                        money("3.45"), money("18.45")),
                new PriceQuoteLine(PriceQuoteLineType.INSURANCE, "Ubezpieczenie", money("5.00"),
                        money("1.15"), money("6.15"))),
                new BigDecimal("23"), PAID_AT.minusSeconds(7200));
    }

    public static InvoiceSeller seller() {
        return new InvoiceSeller(new InvoiceParty("Szybka Paczka Sp. z o.o.", TaxId.of("1234563218"),
                "ul. Żółkiewskiego 5", "00-950", "Łódź", "PL", "biuro@szybkapaczka.pl"),
                "PL61109010140000071219812874");
    }

    public static InvoiceBuyer buyer() {
        return new InvoiceBuyer(new InvoiceParty("Jan Kowalski", BUYER_TAX_ID, "Długa 1", "31-147", "Kraków",
                "PL", "jan@example.com"), "pl");
    }

    public static InvoiceFactory factory() {
        return new InvoiceFactory(new InvoiceNumberFormatter(), ZoneId.of("Europe/Warsaw"),
                Clock.fixed(PAID_AT, ZoneOffset.UTC));
    }

    public static Invoice invoice(final PaymentId paymentId, final long sequence) {
        return factory().create(new InvoiceDraft(paymentId, PRICE_QUOTE_ID, PaymentMethod.CASH, PAID_AT, seller(),
                buyer(), Currency.PLN, List.of(
                new InvoiceLine("Przesyłka standardowa", new BigDecimal("23"), money("15.00"), money("3.45"),
                        money("18.45")))), sequence);
    }

    public static Money money(final String amount) {
        return Money.from(new BigDecimal(amount), Currency.PLN);
    }
}
