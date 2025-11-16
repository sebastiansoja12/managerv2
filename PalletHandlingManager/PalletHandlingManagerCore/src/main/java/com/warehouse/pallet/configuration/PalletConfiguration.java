package com.warehouse.pallet.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.warehouse.pallet.domain.port.primary.PalletPort;
import com.warehouse.pallet.domain.port.primary.PalletPortImpl;
import com.warehouse.pallet.domain.port.secondary.DriverRepository;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.domain.service.DriverStorageService;
import com.warehouse.pallet.domain.service.PalletStorageService;
import com.warehouse.pallet.domain.service.SealNumberService;
import com.warehouse.pallet.domain.service.SealNumberServiceImpl;
import com.warehouse.pallet.infrastructure.adapter.secondary.DriverRepositoryImpl;
import com.warehouse.pallet.infrastructure.adapter.secondary.PalletDocumentReadRepository;
import com.warehouse.pallet.infrastructure.adapter.secondary.PalletRepositoryImpl;

@Configuration
public class PalletConfiguration {


    @Bean
    public PalletPort palletPort(final PalletStorageService palletStorageService,
                                 final DriverStorageService driverStorageService,
                                 final SealNumberService sealNumberService) {
        return new PalletPortImpl(palletStorageService, driverStorageService, sealNumberService);
    }

    @Bean
    public SealNumberService sealNumberService(final PalletRepository palletRepository) {
        return new SealNumberServiceImpl(palletRepository);
    }

    @Bean
    public PalletRepository palletRepository(final PalletDocumentReadRepository repository) {
        return new PalletRepositoryImpl(repository);
    }

    @Bean
    public DriverRepository driverRepository() {
        return new DriverRepositoryImpl();
    }
}
