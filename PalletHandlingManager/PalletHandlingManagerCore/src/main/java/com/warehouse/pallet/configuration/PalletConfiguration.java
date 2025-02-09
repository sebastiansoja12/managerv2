package com.warehouse.pallet.configuration;

import com.warehouse.pallet.domain.port.primary.PalletPort;
import com.warehouse.pallet.domain.port.primary.PalletPortImpl;
import com.warehouse.pallet.domain.port.secondary.PalletRepository;
import com.warehouse.pallet.domain.service.DriverStorageService;
import com.warehouse.pallet.domain.service.PalletStorageService;
import com.warehouse.pallet.infrastructure.adapter.secondary.PalletDocumentReadRepository;
import com.warehouse.pallet.infrastructure.adapter.secondary.PalletRepositoryImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PalletConfiguration {


    @Bean
    public PalletPort palletPort(final PalletStorageService palletStorageService,
                                 final DriverStorageService driverStorageService) {
        return new PalletPortImpl(palletStorageService, driverStorageService);
    }

    @Bean
    public PalletRepository palletRepository(final PalletDocumentReadRepository repository) {
        return new PalletRepositoryImpl(repository);
    }
}
