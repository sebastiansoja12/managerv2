package com.warehouse.shipment.infrastructure.adapter.secondary;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

public interface GenericFeignResourceClient {

	@GetMapping("/{id}")
	ResponseEntity<String> findById(@PathVariable("id") String id);
}
