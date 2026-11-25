package com.warehouse.billing.payment.infrastructure.adapter.primary.api;

import java.util.List;

public record PaymentPageApiResponse(List<PaymentApiResponse> payments,
                                     int page,
                                     int size,
                                     long totalElements) {
}
