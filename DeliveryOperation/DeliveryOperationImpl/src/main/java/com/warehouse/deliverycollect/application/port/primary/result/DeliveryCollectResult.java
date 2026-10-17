package com.warehouse.deliverycollect.application.port.primary.result;

import java.util.List;

import com.warehouse.deliverycollect.domain.vo.CollectResult;

public record DeliveryCollectResult(List<CollectResult> results) {
}
