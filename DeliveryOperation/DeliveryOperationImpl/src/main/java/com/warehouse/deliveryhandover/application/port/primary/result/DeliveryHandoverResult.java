package com.warehouse.deliveryhandover.application.port.primary.result;

import java.util.List;

import com.warehouse.deliveryhandover.domain.vo.HandoverResult;

public record DeliveryHandoverResult(List<HandoverResult> results, boolean recipientSigned) {
}
