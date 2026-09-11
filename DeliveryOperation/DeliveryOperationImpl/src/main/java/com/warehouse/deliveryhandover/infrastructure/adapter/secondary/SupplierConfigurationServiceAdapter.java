package com.warehouse.deliveryhandover.infrastructure.adapter.secondary;

import com.warehouse.commonassets.repository.OperatorContextProvider;
import com.warehouse.commonassets.repository.OperatorDetails;
import com.warehouse.deliveryhandover.application.port.secondary.SupplierConfigurationServicePort;
import com.warehouse.supplier.api.SupplierConfigurationApiService;
import com.warehouse.supplier.api.dto.SupplierConfigurationDto;

public class SupplierConfigurationServiceAdapter implements SupplierConfigurationServicePort {

    private static final String REQUIRE_RECIPIENT_SIGNATURE = "REQUIRE_RECIPIENT_SIGNATURE";

    private final SupplierConfigurationApiService supplierConfigurationApiService;

    private final OperatorContextProvider operatorContextProvider;

    public SupplierConfigurationServiceAdapter(final SupplierConfigurationApiService supplierConfigurationApiService,
                                               final OperatorContextProvider operatorContextProvider) {
        this.supplierConfigurationApiService = supplierConfigurationApiService;
        this.operatorContextProvider = operatorContextProvider;
    }

    @Override
    public boolean isRecipientSignatureRequired() {
        return operatorContextProvider.currentContext()
                .map(OperatorDetails::userId)
                .flatMap(supplierConfigurationApiService::findByUserId)
                .map(configuration -> configuration.isEnabled(REQUIRE_RECIPIENT_SIGNATURE))
                .orElse(false);
    }
}
