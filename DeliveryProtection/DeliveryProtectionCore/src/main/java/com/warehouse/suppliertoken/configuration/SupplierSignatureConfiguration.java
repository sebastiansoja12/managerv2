package com.warehouse.suppliertoken.configuration;


import com.warehouse.suppliertoken.domain.port.primary.SupplierTokenPort;
import com.warehouse.suppliertoken.domain.port.primary.SupplierTokenPortImpl;
import com.warehouse.suppliertoken.domain.port.secondary.ParcelServicePort;
import com.warehouse.suppliertoken.domain.port.secondary.SupplierTokenServicePort;
import com.warehouse.suppliertoken.domain.service.SupplierService;
import com.warehouse.suppliertoken.domain.service.SupplierServiceImpl;
import com.warehouse.suppliertoken.infrastructure.adapter.primary.api.SupplierTokenService;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.ParcelServiceAdapter;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.SupplierTokenAdapter;
import com.warehouse.suppliertoken.infrastructure.adapter.secondary.property.ShipmentProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SupplierSignatureConfiguration {


    @Bean
    public SupplierTokenService supplierTokenService(SupplierTokenPort supplierTokenPort) {
        return new com.warehouse.suppliertoken.infrastructure.adapter.primary.SupplierTokenAdapter(supplierTokenPort);
    }

    @Bean
    public SupplierTokenPort supplierTokenPort(ParcelServicePort parcelServicePort,
                                               SupplierTokenServicePort supplierTokenServicePort) {
        final SupplierService service = new SupplierServiceImpl(supplierTokenServicePort);
        return new SupplierTokenPortImpl(service, parcelServicePort);
    }

    @Bean
    public ParcelServicePort parcelServicePort(ShipmentProperty shipmentProperty) {
        return new ParcelServiceAdapter(shipmentProperty);
    }

    @Bean
    public SupplierTokenServicePort supplierTokenServicePort() {
        return new SupplierTokenAdapter();
    }
}
