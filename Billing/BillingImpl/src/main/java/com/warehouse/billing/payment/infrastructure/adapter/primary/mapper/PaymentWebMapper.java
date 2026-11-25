package com.warehouse.billing.payment.infrastructure.adapter.primary.mapper;

import com.warehouse.billing.api.dto.PriceQuoteIdDto;
import com.warehouse.billing.payment.application.port.primary.command.ConfirmOnlinePaymentCommand;
import com.warehouse.billing.payment.application.port.primary.command.FindPaymentsCommand;
import com.warehouse.billing.payment.application.port.primary.command.RecordCounterPaymentCommand;
import com.warehouse.billing.payment.application.port.primary.result.CheckoutView;
import com.warehouse.billing.payment.application.port.primary.result.PaymentPage;
import com.warehouse.billing.payment.domain.enumeration.PaymentStatus;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.CounterPaymentApiRequest;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.DepartmentIdDto;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.ExternalPaymentApiResponse;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.PaymentApiResponse;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.PaymentIdDto;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.PaymentPageApiResponse;
import com.warehouse.billing.payment.infrastructure.adapter.primary.api.UserIdDto;
import com.warehouse.commonassets.identificator.PaymentId;
import com.warehouse.commonassets.identificator.PriceQuoteId;
import com.warehouse.commonassets.identificator.TaxId;
import com.warehouse.commonassets.identificator.TrackingNumber;
import com.warehouse.commonassets.identificator.WebhookEventId;

import java.util.UUID;

public class PaymentWebMapper {

    public RecordCounterPaymentCommand toCommand(final PriceQuoteId priceQuoteId,
                                                 final CounterPaymentApiRequest request) {
        return new RecordCounterPaymentCommand(priceQuoteId, request.method(),
                request.buyerTaxId() == null || request.buyerTaxId().isBlank()
                        ? null : TaxId.of(request.buyerTaxId().trim()));
    }

    public FindPaymentsCommand toFindCommand(final PaymentStatus status, final PriceQuoteId priceQuoteId,
                                             final int page, final int size) {
        return new FindPaymentsCommand(status, priceQuoteId, page, size);
    }

    public PaymentApiResponse toResponse(final Payment payment) {
        return new PaymentApiResponse(
                new PaymentIdDto(payment.id().value()),
                new PriceQuoteIdDto(payment.priceQuoteId().value()),
                payment.amount().getAmount(),
                payment.amount().getCurrency(),
                payment.channel(),
                payment.method(),
                payment.status(),
                payment.deadline(),
                payment.createdAt(),
                payment.paidAt(),
                payment.recordedBy() == null ? null : new UserIdDto(payment.recordedBy().value()),
                payment.department() == null ? null : new DepartmentIdDto(payment.department().value()),
                payment.buyerTaxId() == null ? null : payment.buyerTaxId().value(),
                payment.refundRequestedAt(),
                payment.refundedAt(),
                payment.refundedBy() == null ? null : new UserIdDto(payment.refundedBy().value()));
    }

    public PaymentPageApiResponse toResponse(final PaymentPage page) {
        return new PaymentPageApiResponse(page.payments().stream().map(this::toResponse).toList(), page.page(),
                page.size(), page.totalElements());
    }

    public TrackingNumber toTrackingNumber(final String trackingNumber) {
        return new TrackingNumber(trackingNumber);
    }

    public ExternalPaymentApiResponse toResponse(final CheckoutView checkout) {
        return new ExternalPaymentApiResponse(checkout.status(), checkout.amount().getAmount(),
                checkout.amount().getCurrency(), checkout.paymentUrl(), checkout.expiresAt());
    }

    public ConfirmOnlinePaymentCommand toFakeConfirmation(final PaymentId paymentId) {
        return new ConfirmOnlinePaymentCommand(paymentId, null, null,
                new WebhookEventId("fake_evt_" + UUID.randomUUID()));
    }
}
