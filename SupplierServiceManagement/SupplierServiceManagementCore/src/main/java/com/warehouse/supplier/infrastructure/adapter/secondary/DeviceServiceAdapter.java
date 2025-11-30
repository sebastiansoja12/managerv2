package com.warehouse.supplier.infrastructure.adapter.secondary;

import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.supplier.domain.port.secondary.DeviceServicePort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.client.RestClient;

@Slf4j
public class DeviceServiceAdapter implements DeviceServicePort {

    private final RestClient restClient;

    private final SupplierConfig supplierConfig;

    public DeviceServiceAdapter(final SupplierConfig supplierConfig) {
        this.supplierConfig = supplierConfig;
        this.restClient = RestClient.builder().baseUrl(supplierConfig.getUrl()).build();
    }

    @Override
    public Result<Void, String> validateDevice(final DeviceId deviceId) {
        final String url = "http://localhost:8080/v2/api/devices/" + deviceId.getValue();
        log.info("Validating device with url: {}", url);
        final ResponseEntity<Object> result =
                restClient
                        .get()
                        .uri(url)
						.header(HttpHeaders.AUTHORIZATION, "Bearer "
								+ SecurityContextHolder.getContext().getAuthentication().getCredentials().toString())
                        .retrieve()
                        .toEntity(Object.class);
        if (result.getStatusCode().is2xxSuccessful()) {
            return Result.success();
        } else {
            return Result.failure("Device with id " + deviceId.getValue() + " not found");
        }
    }
}
