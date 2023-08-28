package com.warehouse.supplier.configuration;

import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.supplier.domain.port.primary.SupplyPort;
import com.warehouse.supplier.domain.port.primary.SupplyPortImpl;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.port.secondary.SupplierServicePort;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorService;
import com.warehouse.supplier.domain.service.SupplierCodeGeneratorServiceImpl;
import com.warehouse.supplier.domain.service.SupplierService;
import com.warehouse.supplier.infrastructure.adapter.secondary.SupplierAdapter;
import com.warehouse.supplier.infrastructure.adapter.secondary.SupplierReadRepository;
import com.warehouse.supplier.infrastructure.adapter.secondary.SupplierRepositoryImpl;
import com.warehouse.supplier.infrastructure.adapter.secondary.mapper.SupplierEntityMapper;

@Configuration
public class SupplierConfiguration {

    @Bean
    public SupplyPort supplyPort(SupplierService service, SupplierCodeGeneratorService generatorService) {
        return new SupplyPortImpl(service, generatorService);
    }

    @Bean
    public SupplierCodeGeneratorService generatorService() {
        return new SupplierCodeGeneratorServiceImpl();
    }

    @Bean
    public SupplierServicePort servicePort() {
        return new SupplierAdapter();
    }

    @Bean
    public SupplierRepository supplierRepository(SupplierReadRepository supplierReadRepository) {
        final SupplierEntityMapper supplierEntityMapper = Mappers.getMapper(SupplierEntityMapper.class);
        return new SupplierRepositoryImpl(supplierEntityMapper, supplierReadRepository);
    }
}
