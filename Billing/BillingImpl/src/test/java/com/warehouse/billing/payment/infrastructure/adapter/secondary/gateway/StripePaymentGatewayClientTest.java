package com.warehouse.billing.payment.infrastructure.adapter.secondary.gateway;

import com.stripe.StripeClient;
import com.stripe.exception.InvalidRequestException;
import com.stripe.exception.StripeException;
import com.stripe.model.StripeObject;
import com.stripe.net.ApiMode;
import com.stripe.net.ApiRequest;
import com.stripe.net.ApiResource;
import com.stripe.net.BaseAddress;
import com.stripe.net.RequestOptions;
import com.stripe.net.StripeResponseGetter;
import com.warehouse.billing.payment.domain.enumeration.CheckoutClosure;
import com.warehouse.billing.payment.domain.enumeration.RefundOutcome;
import com.warehouse.billing.payment.domain.model.Payment;
import com.warehouse.billing.payment.domain.model.PaymentFixture;
import com.warehouse.billing.payment.domain.vo.CheckoutSession;
import com.warehouse.billing.payment.domain.vo.GatewayRefund;
import com.warehouse.billing.payment.domain.exception.PaymentException;
import com.warehouse.billing.payment.domain.service.CheckoutExpiryPolicy;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.lang.reflect.Type;
import java.time.Clock;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class StripePaymentGatewayClientTest {

    @Test
    void shouldTreatMissingCheckoutSessionAsExpired() {
        final StripePaymentGatewayClient client = client(new InvalidRequestException(
                "No such checkout.session: fake_cs_1", "id", "req_1", "resource_missing", 404, null));

        assertThat(client.expireCheckout("fake_cs_1")).isEqualTo(CheckoutClosure.EXPIRED);
    }

    @Test
    void shouldReportGatewayFailureForOtherInvalidRequests() {
        final StripePaymentGatewayClient client = client(new InvalidRequestException(
                "Invalid request", "id", "req_2", "parameter_invalid_empty", 400, null));

        assertThatThrownBy(() -> client.expireCheckout("cs_1"))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.GATEWAY_FAILURE));
    }

    @Test
    void shouldCompleteRefundImmediatelyWhenStripeReportsSucceeded() {
        final GatewayRefund refund = client(new RefundResponseGetter("succeeded", null)).refund(paidPayment());

        assertThat(refund.outcome()).isEqualTo(RefundOutcome.COMPLETED);
        assertThat(refund.gatewayPaymentIntent()).isEqualTo("pi_1");
    }

    @Test
    void shouldReportPendingRefundWhenStripeHasNotSettledIt() {
        final GatewayRefund refund = client(new RefundResponseGetter("pending", null)).refund(paidPayment());

        assertThat(refund.outcome()).isEqualTo(RefundOutcome.PENDING);
    }

    @Test
    void shouldTreatAlreadyRefundedChargeAsCompletedRefund() {
        final GatewayRefund refund = client(new RefundResponseGetter(null, new InvalidRequestException(
                "Charge has already been refunded", null, "req_3", "charge_already_refunded", 400, null)))
                .refund(paidPayment());

        assertThat(refund.outcome()).isEqualTo(RefundOutcome.COMPLETED);
        assertThat(refund.gatewayPaymentIntent()).isEqualTo("pi_1");
    }

    @Test
    void shouldReportPermanentRefundRejectionAsIllegalState() {
        final StripePaymentGatewayClient client = client(new RefundResponseGetter(null, new InvalidRequestException(
                "Invalid amount", "amount", "req_4", "amount_too_large", 400, null)));

        assertThatThrownBy(() -> client.refund(paidPayment()))
                .isInstanceOfSatisfying(PaymentException.class, exception ->
                        assertThat(exception.reason()).isEqualTo(PaymentException.Reason.ILLEGAL_STATE));
    }

    private Payment paidPayment() {
        final Payment payment = PaymentFixture.pending();
        payment.attachCheckout(new CheckoutSession("cs_1", "https://checkout/cs_1",
                PaymentFixture.NOW.plusSeconds(3600)), PaymentFixture.NOW);
        payment.markPaidOnline(PaymentFixture.NOW, "cs_1", "pi_1", () -> {
        });
        return payment;
    }

    private StripePaymentGatewayClient client(final StripeResponseGetter responseGetter) {
        return new StripePaymentGatewayClient(new StripeClient(responseGetter),
                new StripeCheckoutSessionParamsFactory("http://success", "http://cancel", new CheckoutExpiryPolicy()),
                Clock.systemUTC());
    }

    private StripePaymentGatewayClient client(final StripeException failure) {
        return new StripePaymentGatewayClient(new StripeClient(new FailingResponseGetter(failure)),
                new StripeCheckoutSessionParamsFactory("http://success", "http://cancel", new CheckoutExpiryPolicy()),
                Clock.systemUTC());
    }

    private record FailingResponseGetter(StripeException failure) implements StripeResponseGetter {

        @Override
        public <T extends StripeObject> T request(final BaseAddress baseAddress,
                                                  final ApiResource.RequestMethod method, final String path,
                                                  final Map<String, Object> params, final Type typeToken,
                                                  final RequestOptions options, final ApiMode apiMode)
                throws StripeException {
            throw this.failure;
        }

        @Override
        public <T extends StripeObject> T request(final ApiRequest request, final Type typeToken)
                throws StripeException {
            throw this.failure;
        }

        @Override
        public InputStream requestStream(final BaseAddress baseAddress, final ApiResource.RequestMethod method,
                                         final String path, final Map<String, Object> params,
                                         final RequestOptions options, final ApiMode apiMode)
                throws StripeException {
            throw this.failure;
        }
    }

    private record RefundResponseGetter(String refundStatus, StripeException refundFailure)
            implements StripeResponseGetter {

        @Override
        public <T extends StripeObject> T request(final BaseAddress baseAddress,
                                                  final ApiResource.RequestMethod method, final String path,
                                                  final Map<String, Object> params, final Type typeToken,
                                                  final RequestOptions options, final ApiMode apiMode)
                throws StripeException {
            return respond(path, typeToken);
        }

        @Override
        public <T extends StripeObject> T request(final ApiRequest request, final Type typeToken)
                throws StripeException {
            return respond(request.getPath(), typeToken);
        }

        @Override
        public InputStream requestStream(final BaseAddress baseAddress, final ApiResource.RequestMethod method,
                                         final String path, final Map<String, Object> params,
                                         final RequestOptions options, final ApiMode apiMode) {
            throw new UnsupportedOperationException();
        }

        private <T extends StripeObject> T respond(final String path, final Type typeToken) throws StripeException {
            if (!path.contains("/refunds")) {
                return ApiResource.GSON.fromJson("""
                        {"id":"cs_1","object":"checkout.session","payment_intent":"pi_1"}""", typeToken);
            }
            if (this.refundFailure != null) {
                throw this.refundFailure;
            }
            return ApiResource.GSON.fromJson("""
                    {"id":"re_1","object":"refund","status":"%s"}""".formatted(this.refundStatus), typeToken);
        }
    }
}
